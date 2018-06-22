package de.ngloader.core.command;

import de.ngloader.api.database.impl.guild.IWuffyGuild;
import de.ngloader.api.database.impl.user.IWuffyUser;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface ICommandExecutor {

	public CommandResult onCommand(MessageReceivedEvent event, IWuffyGuild guild, IWuffyUser user, String[] args);

	default public void onHelp(MessageReceivedEvent event, IWuffyGuild guild, IWuffyUser user) {
		//TODO send default help list
	};
}