package de.ngloader.core;

import de.ngloader.core.config.IConfig;
import de.ngloader.core.database.ModuleStorageService;
import de.ngloader.core.database.impl.IWuffyGuild;
import de.ngloader.core.database.impl.IWuffyMemeber;
import de.ngloader.core.database.impl.IWuffyUser;
import de.ngloader.core.scheduler.WuffyScheduler;
import de.ngloader.core.util.TickingTask;

public abstract class Core extends TickingTask {

	protected abstract void onLoad();
	protected abstract void onDisable();

	protected abstract void onUpdate();

	public abstract IWuffyGuild getGuild(long guildId);
	public abstract IWuffyUser getUser(long userId);
	public abstract IWuffyMemeber getMember(long guildId, long memberId);

	protected final Thread master;

	protected final IConfig config;

	protected final ModuleStorageService storageService;

	protected final WuffyScheduler scheduler;

	public Core(IConfig config) {
		this.config = config;

		this.storageService = new ModuleStorageService();
		this.scheduler = new WuffyScheduler(this);
		this.master = new Thread(this, "Wuffy Discord Bot - Core");

		this.onLoad();

		this.master.start();
	}

	@Override
	protected void update() {
		this.scheduler.update();

		this.onUpdate();
	}

	@Override
	protected void stop() {
		this.running = false;

		this.onDisable();
	}

	public ModuleStorageService getStorageService() {
		return storageService;
	}

	public WuffyScheduler getScheduler() {
		return scheduler;
	}
}