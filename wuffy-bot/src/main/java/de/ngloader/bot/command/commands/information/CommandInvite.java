package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "invite" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandInvite extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		//TODO database support (use args[0] to set new invite)
	}
}