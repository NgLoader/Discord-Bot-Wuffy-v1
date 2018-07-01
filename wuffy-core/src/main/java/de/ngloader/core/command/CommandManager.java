package de.ngloader.core.command;

import de.ngloader.core.Core;

public abstract class CommandManager <C extends Core> {

	protected abstract void init();

	protected final C core;

	protected CommandExecutor<C, ? extends ICommand> executor;
	protected CommandTrigger<C> trigger;
	protected CommandRegistry registry;

	public CommandManager(C core) {
		this.core = core;

		this.init();

		this.core.getScheduler().runTaskRepeat(core, new Runnable() {

			@Override
			public void run() {
				executor.update();
			}
		}, 0, 5); //4 commands per seconds (20 ticks / 5 = 4)
	}

	public CommandTrigger<C> getTrigger() {
		return this.trigger;
	}

	public CommandExecutor<C, ? extends ICommand> getExecutor() {
		return this.executor;
	}

	public CommandRegistry getRegistry() {
		return this.registry;
	}

	public C getCore() {
		return this.core;
	}
}