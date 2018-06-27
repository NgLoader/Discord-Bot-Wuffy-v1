package de.ngloader.bot.command;

import de.ngloader.core.command.ICommand;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

public interface BotCommand extends ICommand {

	public void execute(WuffyMessageRecivedEvent event, String[] args);
}