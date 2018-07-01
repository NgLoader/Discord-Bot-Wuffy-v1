package de.ngloader.core.console.command;

import de.ngloader.core.console.ConsoleCommand;
import de.ngloader.core.console.ConsoleCommandResult;
import de.ngloader.core.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "exit", "shutdown" }, usage = "exit")
public class ConsoleCommandExit implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		System.exit(0);
		return ConsoleCommandResult.SUCCESS;
	}
}