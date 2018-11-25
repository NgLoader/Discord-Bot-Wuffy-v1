package net.wuffy.bootstrap.command;

import net.wuffy.console.ConsoleCommand;
import net.wuffy.console.ConsoleCommandResult;
import net.wuffy.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "stopi", "stopinstance", "instancestop", "istop" }, usage = "StopInstance <instanceName>")
public class ConsoleCommandStopInstance implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		return ConsoleCommandResult.SYNTAX;
	}
}