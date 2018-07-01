package de.ngloader.core.command;

import de.ngloader.core.Core;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.ITickable;

public abstract class CommandExecutor <C extends Core, COMMAND extends ICommand> implements ITickable {

	protected abstract void execute(WuffyMessageRecivedEvent event, COMMAND command, String[] args);

	protected abstract void queue(WuffyMessageRecivedEvent event, String command, String[] args);

	protected final CommandManager<C> manager;

	public CommandExecutor(CommandManager<C> manager) {
		this.manager = manager;
	}

	public CommandManager<C> getManager() {
		return this.manager;
	}
}