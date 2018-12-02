package net.wuffy.bot.commandOLD;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.wuffy.bot.Wuffy;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.commandOLD.commands.botauthor.CommandTest;

public class CommandRegestry {

	private static final List<Command> COMMANDS = new List<Command>();
	private static final Map<String, Command> ENABLED_COMMANDS = new HashMap<String, Command>();

	private final CommandHandler handler;

	public CommandRegestry(CommandHandler handler) {
		this.handler = handler;

		if(!COMMANDS.containsKey(this.handler.getCore()))
			CommandRegestry.COMMANDS.put(this.handler.getCore(), new CopyOnWriteArrayList<Command>());

		if(!CommandRegestry.ENABLED_COMMANDS.containsKey(handler.getCore())) {
			CommandRegestry.ENABLED_COMMANDS.put(this.handler.getCore(), new ConcurrentHashMap<String, Command>());

			this.enable(new CommandTest(this.handler));
		}
	}

	public void enable(Command command) {
		for(String alias : command.getSettings().aliases())
			CommandRegestry.ENABLED_COMMANDS.get(this.handler.getCore()).put(alias, command);

		if(!CommandRegestry.COMMANDS.get(this.handler.getCore()).contains(command))
			CommandRegestry.COMMANDS.get(this.handler.getCore()).add(command);
	}

	public void disable(Command command) {
		for(String alias : command.getSettings().aliases())
			CommandRegestry.ENABLED_COMMANDS.get(this.handler.getCore()).remove(alias);
	}

	public Command getCommand(String command) {
		return CommandRegestry.ENABLED_COMMANDS.get(this.handler.getCore()).get(command);
	}

	public boolean isDisabled(Command command) {
		if(CommandRegestry.ENABLED_COMMANDS.get(this.handler.getCore()).containsValue(command))
			return false;
		return true;
	}

	public List<Command> getAllCommands() {
		return Collections.unmodifiableList(CommandRegestry.COMMANDS.get(this.handler.getCore()));
	}

	public CommandHandler getHandler() {
		return this.handler;
	}
}