package de.ngloader.bot.command.commands;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "1", "2", "3", "4" })
public class CommandTest implements BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		System.out.println(String.join(", ", args));
	}
}