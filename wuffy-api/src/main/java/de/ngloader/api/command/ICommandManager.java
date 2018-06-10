package de.ngloader.api.command;

public interface ICommandManager {

	public void registerExecutor(ICommandExecutor executor);

	public void registerExecutor(Command command, ICommandExecutor executor);
}