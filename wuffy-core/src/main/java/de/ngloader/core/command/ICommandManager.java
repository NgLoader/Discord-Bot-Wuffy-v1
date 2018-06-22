package de.ngloader.core.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface ICommandManager {

	public void issueCommand(MessageReceivedEvent event);

	public void registerExecutor(ICommandExecutor executor);

	public void registerExecutor(Command command, ICommandExecutor executor);
}