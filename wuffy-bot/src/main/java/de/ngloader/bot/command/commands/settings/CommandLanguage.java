package de.ngloader.bot.command.commands.settings;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "prefix" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandLanguage extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
	}
}