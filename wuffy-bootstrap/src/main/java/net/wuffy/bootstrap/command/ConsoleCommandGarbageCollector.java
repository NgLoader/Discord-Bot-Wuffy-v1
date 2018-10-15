package net.wuffy.bootstrap.command;

import net.wuffy.common.logger.Logger;
import net.wuffy.console.ConsoleCommand;
import net.wuffy.console.ConsoleCommandResult;
import net.wuffy.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "gc", "garbagecollection", "garbagecollector", "garbagec" }, usage = "Garbagecollector")
public class ConsoleCommandGarbageCollector implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		Logger.info("Bootstrap", "Calling Garbage collector");
		System.gc();
		return ConsoleCommandResult.SUCCESS;
	}
}