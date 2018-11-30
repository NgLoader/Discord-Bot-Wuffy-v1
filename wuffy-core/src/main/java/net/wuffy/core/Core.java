package net.wuffy.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.dv8tion.jda.core.AccountType;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.IWuffyPhantomReference;
import net.wuffy.common.util.TickingTask;

public abstract class Core extends TickingTask implements IWuffyPhantomReference {

	protected abstract void onDestroy();

	protected final Long startTimeInMillis = System.currentTimeMillis();

	protected final Thread masterThread;
	protected final Thread shutdownHookThread;

	protected final AccountType accountType;
	protected final CoreConfig config;
	protected final UUID id;

	private List<ITickable> tickables = new ArrayList<ITickable>();

	public Core(UUID id, CoreConfig config, AccountType accountType) {
		super(1000 / 20);

		this.accountType = accountType;
		this.config = config;
		this.id = id;

		WuffyPhantomRefernce.getInstance().add(this, this.id.toString());

		this.masterThread = new Thread(this, String.format("Wuffy Discord Bot - Core (%s)", this.id.toString()));

		this.shutdownHookThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Core.this.onDestroy();
				} catch(Exception e) {
					Logger.fatal("TickingTask", "Error by stopped instance", e);
				}

				Logger.info("TickingTask", "Stopped instance");
			}
		}, String.format("Wuffy Discord Bot - ShutdownHook (%s)", this.id.toString()));

		Runtime.getRuntime().addShutdownHook(this.shutdownHookThread);

		this.masterThread.start();
	}

	@Override
	protected void update() {
		for(ITickable tickable : this.tickables)
			try {
				tickable.update();
			} catch(Exception e) {
				Logger.fatal("TickingTask", String.format("Error by updateing tickable (%s) removing from tickable list!", tickable != null ? tickable.getClass().getSimpleName() : "NULL"), e);

				this.tickables.remove(tickable);
			}
	}

	@Override
	protected void stop() {
		this.running = false;
		System.exit(0);
	}

	public void addTickable(ITickable tickable) {
		this.tickables.add(tickable);
	}

	public void removeTickable(ITickable tickable) {
		this.tickables.remove(tickable);
	}

	public Long getStartTimeInMillis() {
		return this.startTimeInMillis;
	}

	public AccountType getAccountType() {
		return this.accountType;
	}

	public CoreConfig getConfig() {
		return this.config;
	}

	public UUID getId() {
		return this.id;
	}
}