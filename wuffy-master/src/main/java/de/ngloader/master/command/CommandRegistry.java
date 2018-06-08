package de.ngloader.master.command;

import de.ngloader.command.CommandManager;
import de.ngloader.master.Wuffy;

public class CommandRegistry {

	public static void register() {
		CommandManager commandManager = Wuffy.getCommandManager();

		commandManager.registerExecutor(new CommandTest());
	}
}