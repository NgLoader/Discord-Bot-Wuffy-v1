package net.wuffy.core.console.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.wuffy.common.logger.Logger;
import net.wuffy.core.console.ConsoleCommand;
import net.wuffy.core.console.ConsoleCommandManager;
import net.wuffy.core.console.ConsoleCommandResult;
import net.wuffy.core.console.IConsoleCommandExecutor;

/**
 * @author Ingrim4
 */
@ConsoleCommand(aliases = { "help", "h" }, usage = "help [-a | -c | category]")
public class ConsoleCommandHelp implements IConsoleCommandExecutor {

	private final ConsoleCommandManager commandManager;

	public ConsoleCommandHelp(ConsoleCommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public ConsoleCommandResult onCommand(String[] args) {
		var arg = args.length == 1 ? args[0].toLowerCase() : "default";

		if(arg.equals("-a")) {
			Logger.info("\t== List of all commands ==");
			for(var commandInfo : commandManager.getCommands().values())
				Logger.info(String.format("%s - %s", commandInfo.getCommand().aliases()[0], commandInfo.getCommand().usage()));
		} else if(arg.equals("-c")) {
			Logger.info("\t== List of all command categories ==");
			List<String> categories = new ArrayList<String>();
			for(var commandInfo : commandManager.getCommands().values()) {
				String category = commandInfo.getCommand().category();
				if(!categories.contains(category))
					categories.add(category);
			}
			Logger.info(categories.stream().collect(Collectors.joining(", ")));
		} else {
			Logger.info(String.format("\t== List of all %s commands ==", arg));
			var commandInfoList = new ArrayList<>();
			for(var commandInfo : commandManager.getCommands().values())
				if(commandInfoList.contains(commandInfo))
					continue;
				else {
					if(arg.equalsIgnoreCase(commandInfo.getCommand().category()))
						Logger.info(String.format("%s - %s", commandInfo.getCommand().aliases()[0], commandInfo.getCommand().usage()));
					commandInfoList.add(commandInfo);
				}
		}

		return ConsoleCommandResult.SUCCESS;
	}
}