package de.ngloader.bot.command;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.command.commands.CommandTest;
import de.ngloader.bot.command.commands.information.CommandCommands;
import de.ngloader.bot.command.commands.information.CommandHelp;
import de.ngloader.bot.command.commands.information.CommandInvite;
import de.ngloader.bot.command.commands.information.CommandPing;
import de.ngloader.bot.command.commands.information.CommandShardInfo;
import de.ngloader.bot.command.commands.information.CommandStats;
import de.ngloader.bot.command.commands.information.CommandStatus;
import de.ngloader.bot.command.commands.information.CommandVersion;
import de.ngloader.core.command.CommandRegistry;

public class CommandManager extends de.ngloader.core.command.CommandManager<WuffyBot> {

	public CommandManager(WuffyBot core) {
		super(core);
	}

	@Override
	protected void init() {
		this.executor = new CommandExecutor(this);
		this.trigger = new CommandTrigger(this);
		this.registry = new CommandRegistry();

		//TODO Add commands
		this.registry.addCommand(new CommandTest());

		//Information
		this.registry.addCommand(new CommandHelp());
		this.registry.addCommand(new CommandStats());
		this.registry.addCommand(new CommandStatus());
		this.registry.addCommand(new CommandCommands());
		this.registry.addCommand(new CommandVersion());
		this.registry.addCommand(new CommandShardInfo());
		this.registry.addCommand(new CommandInvite());
		this.registry.addCommand(new CommandPing());
	}
}