package de.ngloader.bootstrap.command;

import de.ngloader.core.Core;
import de.ngloader.core.console.ConsoleCommand;
import de.ngloader.core.console.ConsoleCommandResult;
import de.ngloader.core.console.IConsoleCommandExecutor;
import de.ngloader.core.logger.Logger;

@ConsoleCommand(aliases = { "stopi", "stopinstance", "instancestop", "istop" }, usage = "StopInstance <instanceName>")
public class ConsoleCommandStopInstance implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		if(args.length > 0) {
			String instanceName = args[0];

			for(Core core : Core.getInstances()) {
				if(core.getConfig().instanceName.equalsIgnoreCase(instanceName)) {
					Logger.info("Bootstrap", String.format("Stopping instance '%s'", instanceName));
					core.disable();
					return ConsoleCommandResult.SUCCESS;
				}
			}

			Logger.info("Bootstrap", String.format("Instance name '%s' cound't found or is not running", instanceName));
			return ConsoleCommandResult.SUCCESS;
		}
		return ConsoleCommandResult.SYNTAX;
	}
}