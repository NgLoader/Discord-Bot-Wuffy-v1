package net.wuffy.bot.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.wuffy.bot.command.commands.other.CommandTest;

public class CommandRegistry {

	private static final List<Command> COMMANDS = new ArrayList<Command>();
	private static final Map<String, Command> COMMANDS_ACTIV = new HashMap<String, Command>();

	static {
		CommandRegistry.COMMANDS.add(new CommandTest());

		CommandRegistry.COMMANDS.forEach(CommandRegistry::enableCommand);
	}

	public static Command getCommand(String alias) {
		return CommandRegistry.COMMANDS_ACTIV.get(alias);
	}

	public static void disableCommand(Command command) {
		for(String alias : command.getSettings().aliases())
			if(CommandRegistry.COMMANDS_ACTIV.containsKey(alias))
				CommandRegistry.COMMANDS_ACTIV.remove(alias);
	}

	public static void enableCommand(Command command) {
		for(String alias : command.getSettings().aliases())
			CommandRegistry.COMMANDS_ACTIV.put(alias, command);
	}
}