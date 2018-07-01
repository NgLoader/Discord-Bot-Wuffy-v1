package de.ngloader.bot.command;

import de.ngloader.core.command.ICommand;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

public abstract class BotCommand implements ICommand {

	public abstract void execute(WuffyMessageRecivedEvent event, String[] args);

	private boolean commandBlocked;

	public boolean isCommandBlocked() {
		return commandBlocked;
	}

	public void setCommandBlocked(boolean commandBlocked) {
		this.commandBlocked = commandBlocked;
	}
}