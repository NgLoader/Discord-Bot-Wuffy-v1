package de.ngloader.bot.command.commands;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "test" })
public class CommandTest implements BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		event.getChannel().sendMessage("Test: " + String.join(", ", args)).queue();
	}
}