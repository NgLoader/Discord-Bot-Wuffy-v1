package de.ngloader.core;

import de.ngloader.core.config.IConfig;
import de.ngloader.core.scheduler.WuffyScheduler;
import de.ngloader.core.util.TickingTask;

public abstract class Core extends TickingTask {

	protected abstract void onLoad();
	protected abstract void onDisable();

	protected abstract void onUpdate();

	protected final Thread master;

	protected final IConfig config;

	protected final WuffyScheduler scheduler;

	public Core(IConfig config) {
		this.config = config;

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

	public WuffyScheduler getScheduler() {
		return scheduler;
	}
}