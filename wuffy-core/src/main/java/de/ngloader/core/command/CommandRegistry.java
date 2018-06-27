package de.ngloader.core.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.AccountType;

public class CommandRegistry {

	private static final Map<AccountType, Map<String, ICommand>> commands = new HashMap<AccountType, Map<String, ICommand>>();

	public static void addCommand(AccountType type, ICommand command) {
		Command annotation = command.getClass().getAnnotation(Command.class);

		if(annotation == null)
			throw new NullPointerException("Command has no \"Command\" annotation. Class: \"" + command.getClass().getSimpleName() + "\"");

		if(!CommandRegistry.commands.containsKey(type))
			CommandRegistry.commands.put(type, new HashMap<String, ICommand>());
		Arrays.asList(annotation.getAliases()).forEach(alias -> CommandRegistry.commands.get(type).put(alias.toLowerCase(), command));
	}

	public static final ICommand getCommand(AccountType type, String command) {
		if(CommandRegistry.commands.containsKey(type))
			return CommandRegistry.commands.get(type).get(command.toLowerCase());
		return null;
	}

	public static Map<String, ICommand> getCommands(AccountType type) {
		return Collections.unmodifiableMap(CommandRegistry.commands.get(type));
	}

	public static Map<AccountType, Map<String, ICommand>> getCommands() {
		return Collections.unmodifiableMap(CommandRegistry.commands);
	}
}