package de.ngloader.bot.command;

import de.ngloader.bot.WuffyBot;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandTrigger extends de.ngloader.core.command.CommandTrigger<WuffyBot> {

	public CommandTrigger(CommandManager<WuffyBot> manager) {
		super(manager);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();

		if(message.startsWith("~")) //TODO Use prefixes
			onTrigger(new WuffyMessageRecivedEvent(manager.getCore(), event.getJDA(), event.getResponseNumber(), event.getMessage()), message.substring(1).split(" "));
	}
}