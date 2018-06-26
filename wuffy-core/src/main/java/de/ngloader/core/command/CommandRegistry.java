package de.ngloader.core.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.AccountType;

public class CommandRegistry {

	private static final Map<AccountType, Map<String, Command>> commands = new HashMap<AccountType, Map<String, Command>>();

	public static void addCommand(AccountType type, Command command) {
		if(!commands.containsKey(type))
			commands.put(type, new HashMap<String, Command>());
		Arrays.asList(command.getAliases()).forEach(alias -> commands.get(type).put(alias.toLowerCase(), command));
	}

	public static final Command getCommand(AccountType type, String command) {
		if(commands.containsKey(type))
			return commands.get(type).get(command.toLowerCase());
		return null;
	}
}