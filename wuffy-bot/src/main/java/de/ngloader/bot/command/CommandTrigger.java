package de.ngloader.bot.command;

import java.util.Arrays;

import de.ngloader.bot.WuffyBot;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.logger.Logger;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandTrigger extends de.ngloader.core.command.CommandTrigger<WuffyBot> {

	public CommandTrigger(CommandManager<WuffyBot> manager) {
		super(manager);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		var message = event.getMessage().getContentRaw();
		var split = message.split("\\s+");
		var args = Arrays.copyOfRange(split, 1, split.length);
		var command = split[0].toLowerCase();

		var prefixes = Arrays.asList("~", "<@327267953177526273>"); //TODO get prefixes from database and check mention

		for(String prefix : prefixes)
			if(command.startsWith(prefix)) {
				command = command.substring(prefix.length());

				if(command.isEmpty() && args.length > 0) {
					command = args[0].toLowerCase();
					args = Arrays.copyOfRange(args, 1, args.length);
				}

				Logger.debug("Command Trigger", "Command: '" + command + "' args: '" + String.join("', '", args) + "'");
				onTrigger(new WuffyMessageRecivedEvent(this.manager.getCore(), event.getJDA(), event.getResponseNumber(), event.getMessage()), command, args);
				return;
			}
	}
}