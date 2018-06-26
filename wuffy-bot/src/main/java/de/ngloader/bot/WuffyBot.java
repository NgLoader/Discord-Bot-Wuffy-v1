package de.ngloader.bot;

import de.ngloader.bot.command.CommandManager;
import de.ngloader.bot.command.CommandRegestry;

public class WuffyBot {

	private static final CommandManager COMMAND_MANAGER;

	static {
		COMMAND_MANAGER = new CommandManager();
	}

	public static CommandManager getCommandManager() {
		return COMMAND_MANAGER;
	}

	public static void main(String[] args) {
		CommandRegestry.registerCommands();
	}
}