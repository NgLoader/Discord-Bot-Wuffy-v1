package net.wuffy.bootstrap.command;

import net.wuffy.common.logger.Logger;
import net.wuffy.console.ConsoleCommand;
import net.wuffy.console.ConsoleCommandResult;
import net.wuffy.console.IConsoleCommandExecutor;

@ConsoleCommand(aliases = { "developermode", "devmode", "dmode", "dm", "devm", "developerm" }, usage = "DeveloperMode [True|False]")
public class ConsoleCommandDeveloperMode implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		if(args.length > 0) {
			Boolean mode = Boolean.valueOf(args[0]);

			if(mode)
				if(System.getProperty("developerMode").equals("false")) {
					System.setProperty("developerMode", "true");

					Logger.info("Bootstrap", "Successful, enabled developer mode.");
				} else
					Logger.info("Bootstrap", "Sorry, the developer mode is already enabled.");
			else
				if(System.getProperty("developerMode").equals("true")) {
					System.setProperty("developerMode", "false");

					Logger.info("Bootstrap", "Successful, disabled developer mode.");
				} else
					Logger.info("Boostrap", "Sorry, the developer mode is already disabled.");
		} else
			if(System.getProperty("developerMode").equals("false")) {
				System.setProperty("developerMode", "true");

				Logger.info("Bootstrap", "Successful, enabled developer mode.");
			} else {
				System.setProperty("developerMode", "false");

				Logger.info("Bootstrap", "Successful, disabled developer mode.");
			}

		return ConsoleCommandResult.SUCCESS;
	}
}