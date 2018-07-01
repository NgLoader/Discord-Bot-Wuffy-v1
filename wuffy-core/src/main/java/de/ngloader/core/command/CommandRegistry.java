package de.ngloader.core.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {

	protected final Map<String, ICommand> commands = new HashMap<String, ICommand>();

	public void addCommand(ICommand command) {
		var annotation = command.getClass().getAnnotation(Command.class);

		if(annotation == null)
			throw new NullPointerException("Command has no \"Command\" annotation. Class: \"" + command.getClass().getSimpleName() + "\"");

		Arrays.asList(annotation.aliases()).forEach(alias -> this.commands.put(alias.toLowerCase(), command));
	}

	public final ICommand getCommand(String command) {
		return this.commands.get(command.toLowerCase());
	}

	public Map<String, ICommand> getCommands() {
		return Collections.unmodifiableMap(this.commands);
	}
}