package de.ngloader.bootstrap.command;

import de.ngloader.common.logger.Logger;
import de.ngloader.core.console.ConsoleCommand;
import de.ngloader.core.console.ConsoleCommandResult;
import de.ngloader.core.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "gc", "garbagecollection", "garbagecollector", "garbagec" }, usage = "Garbagecollector")
public class ConsoleCommandGarbageCollector implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		Logger.info("Bootstrap", "Calling Garbage collector");
		System.gc();
		return ConsoleCommandResult.SUCCESS;
	}
}