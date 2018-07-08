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
import de.ngloader.bot.command.commands.moderator.CommandBan;
import de.ngloader.bot.command.commands.moderator.CommandClear;
import de.ngloader.bot.command.commands.moderator.CommandHardBan;
import de.ngloader.bot.command.commands.moderator.CommandKick;
import de.ngloader.bot.command.commands.moderator.CommandMute;
import de.ngloader.bot.command.commands.moderator.CommandSoftBan;
import de.ngloader.bot.command.commands.moderator.CommandUnBan;
import de.ngloader.bot.command.commands.moderator.CommandUnMute;
import de.ngloader.bot.command.commands.moderator.CommandVCKick;
import de.ngloader.bot.command.commands.settings.CommandPrefix;
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

		//Moderator
		this.registry.addCommand(new CommandVCKick());
		this.registry.addCommand(new CommandClear());
		this.registry.addCommand(new CommandMute());
		this.registry.addCommand(new CommandUnMute());
		this.registry.addCommand(new CommandKick());
		this.registry.addCommand(new CommandBan());
		this.registry.addCommand(new CommandSoftBan());
		this.registry.addCommand(new CommandHardBan());
		this.registry.addCommand(new CommandUnBan());

		//Settings
		this.registry.addCommand(new CommandPrefix());

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