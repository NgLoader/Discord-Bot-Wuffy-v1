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
import net.wuffy.core.event.EventManagerAdapter;
import net.wuffy.core.jda.IJDA;
import net.wuffy.core.scheduler.WuffyScheduler;

public abstract class Core extends TickingTask implements IWuffyPhantomReference {

	protected final Long startTimeInMillis = System.currentTimeMillis();

	protected final UUID id;

	protected final Thread masterThread;
	protected final Thread shutdownHookThread;

	protected final AccountType accountType;
	protected final WuffyScheduler scheduler;
	protected final IJDA jdaAdapter;

	protected final EventManagerAdapter eventManagerAdapter;

	private List<ITickable> tickables = new ArrayList<ITickable>();

	public Core(UUID id, AccountType accountType, IJDA jdaAdapter) {
		super(1000 / 20);

		this.id = id;
		this.jdaAdapter = jdaAdapter;
		this.accountType = accountType;

		WuffyPhantomRefernce.getInstance().add(this, this.id.toString());

		this.scheduler = new WuffyScheduler(this);
		this.tickables.add(this.scheduler);

		this.masterThread = new Thread(this, String.format("Wuffy Discord Bot - Core (%s)", this.id.toString()));
		this.masterThread.setDaemon(true);

		this.eventManagerAdapter = new EventManagerAdapter();

		this.shutdownHookThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Core.this.stop();
			}
		}, String.format("Wuffy Discord Bot - Core ShutdownHook (%s)", this.id.toString()));

		Runtime.getRuntime().addShutdownHook(this.shutdownHookThread);

		this.masterThread.start();
	}

	@Override
	protected void update() {
		for(ITickable tickable : this.tickables)
			try {
				tickable.update();
			} catch(Exception e) {
				Logger.fatal("Core Thread", String.format("Error by updateing tickable (%s) removing from tickable list!", tickable != null ? tickable.getClass().getSimpleName() : "NULL"), e);

				this.tickables.remove(tickable);
			}
	}

	@Override
	protected void stop() {
		this.running = false;

		this.scheduler.cancelAllTasks();
		this.jdaAdapter.logout();

		Logger.info("Core", String.format("Stopped instance (%s)", this.id.toString()));
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

	public Long getStartTimeInMillis() {
		return this.startTimeInMillis;
	}

	public IJDA getJdaAdapter() {
		return this.jdaAdapter;
	}

	public <T extends IJDA> T getJdaAdapter(Class<T> jdaProviderClass) {
		return jdaProviderClass.cast(this.jdaAdapter);
	}

	public WuffyScheduler getScheduler() {
		return this.scheduler;
	}

	public <T extends WuffyScheduler> T getScheduler(Class<T> schedulerClass) {
		return schedulerClass.cast(this.scheduler);
	}

	public EventManagerAdapter getEventManagerAdapter() {
		return this.eventManagerAdapter;
	}

	public UUID getId() {
		return this.id;
	}
}