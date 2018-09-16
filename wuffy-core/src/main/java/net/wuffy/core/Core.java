package net.wuffy.core;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.entities.User;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.IWuffyPhantomReference;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.TickingTask;
import net.wuffy.core.database.IStorageService;
import net.wuffy.core.database.StorageService;
import net.wuffy.core.database.impl.IExtensionGuild;
import net.wuffy.core.database.impl.IExtensionLang;
import net.wuffy.core.database.impl.IExtensionUser;
import net.wuffy.core.jda.IJDAAdapter;
import net.wuffy.core.lang.I18n;
import net.wuffy.core.scheduler.WuffyScheduler;

public abstract class Core extends TickingTask implements IWuffyPhantomReference {

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

	protected final Long startTimeInMillis = System.currentTimeMillis();

	protected final CoreConfig config;

	protected final Thread masterThread;
	protected final Thread shutdownHookThread;

	protected final AccountType accountType;

	protected final IJDAAdapter jdaAdapter;
	protected final StorageService storageService;
	protected final WuffyScheduler scheduler;
	protected final I18n i18n;

	private List<ITickable> tickables = new ArrayList<ITickable>();

	private boolean enabled = false;

	public Core(CoreConfig config, AccountType accountType, Class<? extends IJDAAdapter> jdaAdapter) {
		super(1000 / 20);

		WuffyPhantomRefernce.getInstance().add(this, config != null ? config.instanceName : "UNKNOWN");

		this.config = config;
		this.accountType = accountType;

		try {
			this.jdaAdapter = jdaAdapter.getConstructor(Core.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new Error(e);
		}

		this.scheduler = new WuffyScheduler(this);
		this.storageService = new StorageService(this, config.database);
		this.i18n = new I18n(this);

		this.masterThread = new Thread(this, String.format("Wuffy Discord Bot - Core (%s)", this.config.instanceName));
		this.masterThread.setDaemon(true);

		this.shutdownHookThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Core.this.stop();
			}
		}, String.format("Wuffy Discord Bot - Core ShutdownHook (%s)", this.config.instanceName));

		Runtime.getRuntime().addShutdownHook(this.shutdownHookThread);

		this.storageService.registerExtension("guild", IExtensionGuild.class);
		this.storageService.registerExtension("user", IExtensionUser.class);
		this.storageService.registerExtension("lang", IExtensionLang.class);

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
		Core.INSTANCES.remove(this);

		this.running = false;

		this.onDisable();

		this.scheduler.cancelAllTasks();
		this.jdaAdapter.logout();
		this.storageService.disable();

		Logger.info("Core", String.format("Stopped instance (%s)", this.config != null ? this.config.instanceName : "NULL"));
	}

	public void enable() {
		if(enabled)
			return;
		enabled = true;

		try {
			this.storageService.enable();
			this.i18n.loadLangs(this.storageService.getExtension(IExtensionLang.class));
			this.jdaAdapter.login();

			onEnable();
		} catch(Exception e) {
			e.printStackTrace();
			Logger.warn("Core", "Failed to login");
			stop();
		}
	}

	public void disable() {
		try {
			System.out.println(String.format("Stopping instance (%s)", this.config != null ? this.config.instanceName : "NULL"));

			Runtime.getRuntime().removeShutdownHook(this.shutdownHookThread);

			this.running = false;
		} catch(Exception e) {
			Logger.err("Core", String.format("Error by disable core (%s)", this.config != null ? this.config.instanceName : "NULL"));

			if(this.masterThread != null)
				try {
					this.masterThread.interrupt();
				} catch(Exception e2) {
					Logger.err("Core", String.format("Error by interrupt core (%s)", this.config != null ? this.config.instanceName : "NULL"));
				}
		}
	}

	public boolean isAdmin(User user) {
		return isAdmin(user.getIdLong());
	}

	public boolean isAdmin(Long longId) {
		return this.config.admins.contains(longId);
	}

	public void addTickable(ITickable tickable) {
		this.tickables.add(tickable);
	}

	public void removeTickable(ITickable tickable) {
		this.tickables.remove(tickable);
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public AccountType getAccountType() {
		return this.accountType;
	}

	public Long getStartTimeInMillis() {
		return this.startTimeInMillis;
	}

	public IJDAAdapter getJdaAdapter() {
		return this.jdaAdapter;
	}

	public <T extends IJDAAdapter> T getJdaAdapter(Class<T> jdaProviderClass) {
		return jdaProviderClass.cast(this.jdaAdapter);
	}

	public CoreConfig getConfig() {
		return this.config;
	}

	public <T extends CoreConfig> T getConfig(Class<T> coreConfigClass) {
		return coreConfigClass.cast(this.config);
	}

	public StorageService getStorageService() {
		return this.storageService;
	}

	public <T extends IStorageService> T getStorageService(Class<T> storageServiceClass) {
		return storageServiceClass.cast(this.storageService);
	}

	public WuffyScheduler getScheduler() {
		return this.scheduler;
	}

	public <T extends WuffyScheduler> T getScheduler(Class<T> schedulerClass) {
		return schedulerClass.cast(this.scheduler);
	}

	public I18n getI18n() {
		return this.i18n;
	}

	public <T extends I18n> T getI18n(Class<T> i18nClass) {
		return i18nClass.cast(this.i18n);
	}
}