package net.wuffy.bootstrap.command;

import net.wuffy.common.logger.Logger;
import net.wuffy.console.ConsoleCommand;
import net.wuffy.console.ConsoleCommandResult;
import net.wuffy.console.IConsoleCommandExecutor;
import net.wuffy.core.Core;

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