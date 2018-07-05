package de.ngloader.bootstrap.command;

import de.ngloader.bootstrap.Wuffy;
import de.ngloader.bot.BotConfig;
import de.ngloader.bot.WuffyBot;
import de.ngloader.client.ClientConfig;
import de.ngloader.client.WuffyClient;
import de.ngloader.core.Core;
import de.ngloader.core.console.ConsoleCommand;
import de.ngloader.core.console.ConsoleCommandResult;
import de.ngloader.core.console.IConsoleCommandExecutor;
import de.ngloader.core.logger.Logger;

@ConsoleCommand(aliases = { "starti", "startinstance", "instancestart", "istart" }, usage = "StartInstance <instanceName>")
public class ConsoleCommandStartInstance implements IConsoleCommandExecutor {

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		if(args.length > 0) {
			String instanceName = args[0];

			if(Core.getInstances().stream().anyMatch(instance -> instance.getConfig().instanceName.equalsIgnoreCase(instanceName))) {
				Logger.info("Bootstrap", String.format("Instance '%s' is already running", instanceName));
				return ConsoleCommandResult.SUCCESS;
			}

			for(ClientConfig clientConfig : Wuffy.getInstance().getConfig().clients) {
				if(!clientConfig.instanceName.equalsIgnoreCase(instanceName))
					continue;

				Logger.info("Bootstrap", String.format("Starting client instance '%s'", clientConfig.instanceName));
				new WuffyClient(clientConfig).enable();
				return ConsoleCommandResult.SUCCESS;
			}

			for(BotConfig botConfig : Wuffy.getInstance().getConfig().bots) {
				if(!botConfig.instanceName.equalsIgnoreCase(instanceName))
					continue;

				Logger.info("Bootstrap", String.format("Starting bot instance '%s'", botConfig.instanceName));
				new WuffyBot(botConfig).enable();
				return ConsoleCommandResult.SUCCESS;
			}

			Logger.info("Bootstrap", String.format("Instance name '%s' cound't found", instanceName));
			return ConsoleCommandResult.SUCCESS;
		}
		return ConsoleCommandResult.SYNTAX;
	}
}