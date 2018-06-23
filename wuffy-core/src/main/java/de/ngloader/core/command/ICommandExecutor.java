package de.ngloader.core.command;

import de.ngloader.core.database.impl.guild.WuffyGuild;
import de.ngloader.core.database.impl.user.WuffyUser;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface ICommandExecutor {

	public CommandResult onCommand(MessageReceivedEvent event, WuffyGuild guild, WuffyUser user, String[] args);

	default public void onHelp(MessageReceivedEvent event, WuffyGuild guild, WuffyUser user) {
		//TODO send default help list
	};
}