package de.ngloader.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public interface ICommandExecutor {

	public CommandResult onCommand(GuildMessageReceivedEvent event, String[] args);

	public void onHelp(GuildMessageReceivedEvent event);
}