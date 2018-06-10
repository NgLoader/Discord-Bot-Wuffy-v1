package de.ngloader.api.command;

import de.ngloader.api.database.impl.guild.IWuffyGuild;
import de.ngloader.api.database.impl.user.IWuffyUser;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public interface ICommandExecutor {

	public CommandResult onCommand(GuildMessageReceivedEvent event, IWuffyGuild guild, IWuffyUser user, String[] args);

	default public void onHelp(GuildMessageReceivedEvent event, IWuffyGuild guild, IWuffyUser user) {
		//TODO send default help list
	};
}