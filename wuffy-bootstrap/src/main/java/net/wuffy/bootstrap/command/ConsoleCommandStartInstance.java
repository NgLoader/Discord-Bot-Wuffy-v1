package net.wuffy.bootstrap.command;

import net.wuffy.bootstrap.Wuffy;
import net.wuffy.bot.BotConfig;
import net.wuffy.bot.WuffyBot;
import net.wuffy.client.ClientConfig;
import net.wuffy.client.WuffyClient;
import net.wuffy.common.logger.Logger;
import net.wuffy.console.ConsoleCommand;
import net.wuffy.console.ConsoleCommandResult;
import net.wuffy.console.IConsoleCommandExecutor;
import net.wuffy.core.Core;

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

				clientConfig.admins.addAll(Wuffy.getInstance().getConfig().admins);
				new WuffyClient(clientConfig).enable();
				return ConsoleCommandResult.SUCCESS;
			}

			for(BotConfig botConfig : Wuffy.getInstance().getConfig().bots) {
				if(!botConfig.instanceName.equalsIgnoreCase(instanceName))
					continue;

				Logger.info("Bootstrap", String.format("Starting bot instance '%s'", botConfig.instanceName));

				botConfig.admins.addAll(Wuffy.getInstance().getConfig().admins);
				new WuffyBot(botConfig).enable();
				return ConsoleCommandResult.SUCCESS;
			}

			Logger.info("Bootstrap", String.format("Instance name '%s' cound't found", instanceName));
			return ConsoleCommandResult.SUCCESS;
		}
		return ConsoleCommandResult.SYNTAX;
	}
}