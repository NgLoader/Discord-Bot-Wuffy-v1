package de.ngloader.bot.command.commands.settings;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "permission", "permissions", "perm", "perms"})
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandPermission extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
	}
}