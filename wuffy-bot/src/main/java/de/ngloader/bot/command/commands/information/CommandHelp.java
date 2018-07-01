package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "help", "h" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandHelp extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		//TODO fillout
		this.setCommandBlocked(true);
	}
}