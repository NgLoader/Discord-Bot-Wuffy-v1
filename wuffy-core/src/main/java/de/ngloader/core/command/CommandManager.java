package de.ngloader.core.command;

import de.ngloader.core.Core;
import de.ngloader.core.scheduler.WuffyTask;

public class CommandManager <C extends Core> {

	protected final C core;

	protected final CommandExecutor<C, ? extends ICommand> executor;
	protected final CommandTrigger<C> trigger;

	public CommandManager(C core, CommandExecutor<C, ? extends ICommand> executor, CommandTrigger<C> trigger) {
		this.core = core;
		this.executor = executor;
		this.trigger = trigger;

		this.core.getScheduler().runTaskRepeat(core, new WuffyTask() {

			@Override
			public void onRun() {
				executor.update();
			}
		}, 0, 5); //4 commands per seconds (20 ticks / 5 = 4)
	}

	public C getCore() {
		return this.core;
	}
}