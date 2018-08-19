package net.wuffy.core.console.command;

import net.wuffy.core.console.ConsoleCommand;
import net.wuffy.core.console.ConsoleCommandResult;
import net.wuffy.core.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "exit", "shutdown" }, usage = "exit")
public class ConsoleCommandExit implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		System.exit(0);
		return ConsoleCommandResult.SUCCESS;
	}
}