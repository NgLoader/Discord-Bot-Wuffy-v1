package de.ngloader.master.command;

import de.ngloader.api.command.ICommandManager;
import de.ngloader.master.Wuffy;

public class CommandRegistry {

	public static void register() {
		ICommandManager commandManager = Wuffy.getCommandManager();

		commandManager.registerExecutor(new CommandTest());
	}
}