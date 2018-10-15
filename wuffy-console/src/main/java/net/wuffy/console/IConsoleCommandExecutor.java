package net.wuffy.console;

/**
 * @author Ingrim4
 */
public interface IConsoleCommandExecutor {
	public ConsoleCommandResult onCommand(String[] args);
}