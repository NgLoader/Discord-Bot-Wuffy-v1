package net.wuffy.bootstrap.command;

import net.wuffy.console.ConsoleCommand;
import net.wuffy.console.ConsoleCommandResult;
import net.wuffy.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "starti", "startinstance", "instancestart", "istart" }, usage = "StartInstance <instanceName>")
public class ConsoleCommandStartInstance implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		return ConsoleCommandResult.SYNTAX;
	}
}