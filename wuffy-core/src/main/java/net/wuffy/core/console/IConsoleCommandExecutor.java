package net.wuffy.core.console;

/**
 * @author Ingrim4
 */
public interface IConsoleCommandExecutor {
	public ConsoleCommandResult onCommand(String[] args);
}