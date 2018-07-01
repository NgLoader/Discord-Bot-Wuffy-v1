package de.ngloader.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import de.ngloader.core.config.IConfig;
import de.ngloader.core.database.StorageService;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.locale.LocaleStorage;
import de.ngloader.core.database.mongo.MongoStorage;
import de.ngloader.core.database.sql.SQLStorage;
import de.ngloader.core.jda.IJDAAdapter;
import de.ngloader.core.logger.Logger;
import de.ngloader.core.scheduler.WuffyScheduler;
import de.ngloader.core.util.ITickable;
import de.ngloader.core.util.TickingTask;
import net.dv8tion.jda.core.AccountType;

public abstract class Core extends TickingTask {

	private static final List<Core> INSTANCES = new CopyOnWriteArrayList<Core>();

	public static List<Core> getBots() {
		return Core.INSTANCES.stream().filter(instance -> instance.accountType == AccountType.BOT).collect(Collectors.toList());
	}

	public static List<Core> getClients() {
		return Core.INSTANCES.stream().filter(instance -> instance.accountType == AccountType.CLIENT).collect(Collectors.toList());
	}

	public static List<Core> getInstances() {
		return Core.INSTANCES;
	}

	protected abstract void onEnable();
	protected abstract void onDisable();

	protected final Thread masterThread;

	protected final IConfig config;

	protected final AccountType accountType;

	protected final IJDAAdapter jdaAdapter;
	protected final StorageService storageService;
	protected final WuffyScheduler scheduler;

	private List<ITickable> tickables = new ArrayList<ITickable>();

	private boolean enabled = false;

	public Core(CoreConfig config, AccountType accountType, Class<? extends IJDAAdapter> jdaAdapter) {
		this.config = config;
		this.accountType = accountType;
		try {
			this.jdaAdapter = jdaAdapter.getConstructor(Core.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new Error(e);
		}

		this.scheduler = new WuffyScheduler(this);
		this.storageService = new StorageService(config.database);
		this.masterThread = new Thread(this, "Wuffy Discord Bot - Core");

		this.storageService.registerExtension("guild", IExtensionGuild.class);
		this.storageService.registerExtension("user", IExtensionUser.class);
		this.storageService.registerExtension("lang", IExtensionLang.class);

		if(config.database.mongo.enabled)
			this.storageService.registerStorage(MongoStorage.class, "mongo", new MongoStorage(config.database.mongo));
		if(config.database.sql.enabled)
			this.storageService.registerStorage(SQLStorage.class, "sql", new SQLStorage(config.database.sql));
		if(config.database.locale.enabled)
			this.storageService.registerStorage(LocaleStorage.class, "locale", new LocaleStorage(config.database.locale));

		Core.INSTANCES.add(this);

		this.masterThread.start();
	}

	@Override
	protected void update() {
		this.scheduler.update();

		this.tickables.forEach(ITickable::update);
	}

	@Override
	protected void stop() {
		this.onDisable();

		this.scheduler.cancelAllTasks();
		this.storageService.disable();
		this.jdaAdapter.logout();

		Core.INSTANCES.remove(this);
	}

	public void enable() {
		if(enabled)
			return;
		enabled = true;

		try {
			this.jdaAdapter.login();
			onEnable();
		} catch(Exception e) {
			Logger.warn("Core", "Failed to login");
			stop();
		}
	}

	public void destroy() {
		if(!this.running)
			return;
		this.running = false;
	}

	public void addTickable(ITickable tickable) {
		this.tickables.add(tickable);
	}

	public void removeTickable(ITickable tickable) {
		this.tickables.remove(tickable);
	}

	public <T extends IJDAAdapter> T getJdaAdapter(Class<T> jdaProviderClass) {
		return jdaProviderClass.cast(this.jdaAdapter);
	}

	public IConfig getConfig() {
		return config;
	}

	public AccountType getAccountType() {
		return this.accountType;
	}

	public StorageService getStorageService() {
		return this.storageService;
	}

	public WuffyScheduler getScheduler() {
		return this.scheduler;
	}

	public boolean isEnabled() {
		return enabled;
	}
}