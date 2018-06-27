package de.ngloader.core.command;

import de.ngloader.core.Core;
import de.ngloader.core.scheduler.WuffyTask;

public abstract class CommandManager <C extends Core> {

	protected abstract void init();

	protected final C core;

	protected CommandExecutor<C, ? extends ICommand> executor;
	protected CommandTrigger<C> trigger;

	public CommandManager(C core) {
		this.core = core;

		this.init();

		this.core.getScheduler().runTaskRepeat(core, new WuffyTask() {

			@Override
			public void onRun() {
				executor.update();
			}
		}, 0, 5); //4 commands per seconds (20 ticks / 5 = 4)
	}

	public CommandTrigger<C> getTrigger() {
		return trigger;
	}

	public CommandExecutor<C, ? extends ICommand> getExecutor() {
		return executor;
	}

	public C getCore() {
		return this.core;
	}
}