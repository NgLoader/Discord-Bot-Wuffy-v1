package net.wuffy.bootstrap.command;

import net.wuffy.common.logger.Logger;
import net.wuffy.core.console.ConsoleCommand;
import net.wuffy.core.console.ConsoleCommandResult;
import net.wuffy.core.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "gc", "garbagecollection", "garbagecollector", "garbagec" }, usage = "Garbagecollector")
public class ConsoleCommandGarbageCollector implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		Logger.info("Bootstrap", "Calling Garbage collector");
		System.gc();
		return ConsoleCommandResult.SUCCESS;
	}
}