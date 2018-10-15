package net.wuffy.console.commands;

import net.wuffy.console.ConsoleCommand;
import net.wuffy.console.ConsoleCommandResult;
import net.wuffy.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "exit", "shutdown" }, usage = "exit")
public class ConsoleCommandExit implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		System.exit(0);
		return ConsoleCommandResult.SUCCESS;
	}
}