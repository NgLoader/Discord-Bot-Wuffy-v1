package de.ngloader.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import de.ngloader.core.config.IConfig;
import de.ngloader.core.database.ModuleStorageService;
import de.ngloader.core.database.impl.IWuffyGuild;
import de.ngloader.core.database.impl.IWuffyMemeber;
import de.ngloader.core.database.impl.IWuffyUser;
import de.ngloader.core.scheduler.WuffyScheduler;
import de.ngloader.core.util.ITickable;
import de.ngloader.core.util.TickingTask;
import net.dv8tion.jda.core.AccountType;

public abstract class Core extends TickingTask {

	private static final List<Core> INSTANCES = new CopyOnWriteArrayList<>();

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

	public abstract IWuffyGuild getGuild(long guildId);
	public abstract IWuffyUser getUser(long userId);
	public abstract IWuffyMemeber getMember(long guildId, long memberId);

	protected final Thread masterThread;

	protected final IConfig config;

	protected final AccountType accountType;

	protected final ModuleStorageService storageService;

	protected final WuffyScheduler scheduler;

	private List<ITickable> tickables = new ArrayList<ITickable>();

	private boolean enabled = false;

	public Core(CoreConfig config, AccountType accountType) {
		this.config = config;
		this.accountType = accountType;

		this.scheduler = new WuffyScheduler(this);
		this.storageService = new ModuleStorageService(config.database);
		this.masterThread = new Thread(this, "Wuffy Discord Bot - Core");

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
		this.running = false;
		this.scheduler.cancelAllTasks();

		this.onDisable();

		this.storageService.disable();
	}

	public void enable() {
		if(enabled)
			return;
		enabled = true;
		onEnable();
	}

	public void addTickable(ITickable tickable) {
		this.tickables.add(tickable);
	}

	public void removeTickable(ITickable tickable) {
		this.tickables.remove(tickable);
	}

	public AccountType getAccountType() {
		return this.accountType;
	}

	public ModuleStorageService getStorageService() {
		return this.storageService;
	}

	public WuffyScheduler getScheduler() {
		return this.scheduler;
	}

	public boolean isEnabled() {
		return enabled;
	}
}