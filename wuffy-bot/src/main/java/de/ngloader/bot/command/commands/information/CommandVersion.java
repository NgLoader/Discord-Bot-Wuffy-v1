package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "version", "ver" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandVersion extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		event.getChannel().sendMessage("Version: " + event.getCore().getConfig().instanceVersion).queue();
	}
}