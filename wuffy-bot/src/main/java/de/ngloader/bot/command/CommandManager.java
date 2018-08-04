package de.ngloader.bot.command;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.command.commands.image.CommandCat;
import de.ngloader.bot.command.commands.image.CommandDog;
import de.ngloader.bot.command.commands.image.CommandGarfield;
import de.ngloader.bot.command.commands.image.rra.CommandRRACry;
import de.ngloader.bot.command.commands.image.rra.CommandRRAHug;
import de.ngloader.bot.command.commands.image.rra.CommandRRAKiss;
import de.ngloader.bot.command.commands.image.rra.CommandRRALewd;
import de.ngloader.bot.command.commands.image.rra.CommandRRALick;
import de.ngloader.bot.command.commands.image.rra.CommandRRANom;
import de.ngloader.bot.command.commands.image.rra.CommandRRANyan;
import de.ngloader.bot.command.commands.image.rra.CommandRRAOwo;
import de.ngloader.bot.command.commands.image.rra.CommandRRAPat;
import de.ngloader.bot.command.commands.image.rra.CommandRRAPout;
import de.ngloader.bot.command.commands.image.rra.CommandRRARem;
import de.ngloader.bot.command.commands.image.rra.CommandRRASlap;
import de.ngloader.bot.command.commands.image.rra.CommandRRASmug;
import de.ngloader.bot.command.commands.image.rra.CommandRRAStare;
import de.ngloader.bot.command.commands.image.rra.CommandRRATickle;
import de.ngloader.bot.command.commands.image.rra.CommandRRATriggered;
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
import de.ngloader.bot.command.commands.nsfw.CommandE621;
import de.ngloader.bot.command.commands.nsfw.CommandRule34;
import de.ngloader.bot.command.commands.settings.CommandAutoRole;
import de.ngloader.bot.command.commands.settings.CommandLanguage;
import de.ngloader.bot.command.commands.settings.CommandMention;
import de.ngloader.bot.command.commands.settings.CommandMessage;
import de.ngloader.bot.command.commands.settings.CommandNotification;
import de.ngloader.bot.command.commands.settings.CommandPermission;
import de.ngloader.bot.command.commands.settings.CommandPrefix;
import de.ngloader.bot.command.commands.settings.CommandRename;
import de.ngloader.bot.command.commands.settings.CommandTwitch;
import de.ngloader.bot.command.commands.settings.CommandYoutube;
import de.ngloader.bot.command.commands.utility.CommandGuildInfo;
import de.ngloader.bot.command.commands.utility.CommandUserInfo;
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
//		this.registry.addCommand(new CommandTest());

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
		this.registry.addCommand(new CommandRename());

		//Settings
		this.registry.addCommand(new CommandPrefix());
		this.registry.addCommand(new CommandLanguage());
		this.registry.addCommand(new CommandAutoRole());
//		this.registry.addCommand(new CommandAutoPrune());
		this.registry.addCommand(new CommandPermission());
		this.registry.addCommand(new CommandMessage());
		this.registry.addCommand(new CommandMention());
		this.registry.addCommand(new CommandTwitch());
		this.registry.addCommand(new CommandYoutube());
		this.registry.addCommand(new CommandNotification());

		//Information
		this.registry.addCommand(new CommandHelp());
		this.registry.addCommand(new CommandStats());
		this.registry.addCommand(new CommandStatus());
		this.registry.addCommand(new CommandCommands());
		this.registry.addCommand(new CommandVersion());
		this.registry.addCommand(new CommandShardInfo());
		this.registry.addCommand(new CommandInvite());
		this.registry.addCommand(new CommandPing());

		//Utility
		this.registry.addCommand(new CommandUserInfo());
//		this.registry.addCommand(new CommandServerInfo());
		this.registry.addCommand(new CommandGuildInfo());
//		this.registry.addCommand(new CommandGlobalInfo());

		//NSFW
		this.registry.addCommand(new CommandRule34());
		this.registry.addCommand(new CommandE621());

		//Image
		this.registry.addCommand(new CommandCat());
		this.registry.addCommand(new CommandDog());
		this.registry.addCommand(new CommandGarfield());
		this.registry.addCommand(new CommandRRACry());
		this.registry.addCommand(new CommandRRAHug());
		this.registry.addCommand(new CommandRRAKiss());
		this.registry.addCommand(new CommandRRALewd());
		this.registry.addCommand(new CommandRRALick());
		this.registry.addCommand(new CommandRRANom());
		this.registry.addCommand(new CommandRRANyan());
		this.registry.addCommand(new CommandRRAOwo());
		this.registry.addCommand(new CommandRRAPat());
		this.registry.addCommand(new CommandRRAPout());
		this.registry.addCommand(new CommandRRARem());
		this.registry.addCommand(new CommandRRASlap());
		this.registry.addCommand(new CommandRRASmug());
		this.registry.addCommand(new CommandRRAStare());
		this.registry.addCommand(new CommandRRATickle());
		this.registry.addCommand(new CommandRRATriggered());
	}
}