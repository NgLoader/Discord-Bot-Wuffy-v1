package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "ping", "pong" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandPing extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		var start = System.currentTimeMillis();

		event.getChannel().sendMessage("Pong: calculating").complete().editMessage("Ping: " + (System.currentTimeMillis() - start) + "ms").queue();
	}
}