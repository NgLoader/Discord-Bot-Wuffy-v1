package de.ngloader.bot.command.commands;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "test" })
@CommandConfig(category = CommandCategory.OTHER)
public class CommandTest extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
	}
}