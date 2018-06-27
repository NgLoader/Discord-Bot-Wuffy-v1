package de.ngloader.bot.command;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.command.commands.CommandTest;
import de.ngloader.core.command.CommandRegistry;
import net.dv8tion.jda.core.AccountType;

public class CommandManager extends de.ngloader.core.command.CommandManager<WuffyBot> {

	static {
		CommandRegistry.addCommand(AccountType.BOT, new CommandTest());
	}

	public CommandManager(WuffyBot core) {
		super(core);
	}

	@Override
	protected void init() {
		this.executor = new CommandExecutor(this);
		this.trigger = new CommandTrigger(this);
	}
}