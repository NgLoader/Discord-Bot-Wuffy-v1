package de.ngloader.bootstrap.command;

import de.ngloader.core.console.ConsoleCommand;
import de.ngloader.core.console.ConsoleCommandResult;
import de.ngloader.core.console.IConsoleCommandExecutor;
import de.ngloader.core.logger.Logger;

@ConsoleCommand(aliases = { "developermode", "devmode", "dmode", "dm", "devm", "developerm" }, usage = "DeveloperMode [True|False]")
public class ConsoleCommandDeveloperMode implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		if(args.length > 0) {
			Boolean mode = Boolean.valueOf(args[0]);

			if(mode)
				if(System.getProperty("developerMode") == null) {
					System.setProperty("developerMode", "true");

					Logger.info("Bootstrap", "Successful, enabled developer mode.");
				} else
					Logger.info("Bootstrap", "Sorry, the developer mode is already enabled.");
			else
				if(System.getProperty("developerMode") != null) {
					System.clearProperty("developerMode");

					Logger.info("Bootstrap", "Successful, disabled developer mode.");
				} else
					Logger.info("Boostrap", "Sorry, the developer mode is already disabled.");
		} else
			if(System.getProperty("developerMode") == null) {
				System.setProperty("developerMode", "true");

				Logger.info("Bootstrap", "Successful, enabled developer mode.");
			} else {
				System.clearProperty("developerMode");

				Logger.info("Bootstrap", "Successful, disabled developer mode.");
			}

		return ConsoleCommandResult.SUCCESS;
	}
}