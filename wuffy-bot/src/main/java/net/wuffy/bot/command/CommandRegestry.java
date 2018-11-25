package net.wuffy.bot.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.admin.CommandBan;
import net.wuffy.bot.command.commands.admin.CommandClear;
import net.wuffy.bot.command.commands.admin.CommandHardBan;
import net.wuffy.bot.command.commands.admin.CommandKick;
import net.wuffy.bot.command.commands.admin.CommandMute;
import net.wuffy.bot.command.commands.admin.CommandSoftBan;
import net.wuffy.bot.command.commands.admin.CommandUnBan;
import net.wuffy.bot.command.commands.admin.CommandUnMute;
import net.wuffy.bot.command.commands.admin.CommandVCKick;
import net.wuffy.bot.command.commands.botauthor.CommandMaintaince;
import net.wuffy.bot.command.commands.botauthor.CommandTest;
import net.wuffy.bot.command.commands.fun.Command8Ball;
import net.wuffy.bot.command.commands.fun.CommandAscii;
import net.wuffy.bot.command.commands.fun.CommandCoinflip;
import net.wuffy.bot.command.commands.fun.CommandLoading;
import net.wuffy.bot.command.commands.fun.CommandTableflip;
import net.wuffy.bot.command.commands.image.CommandCat;
import net.wuffy.bot.command.commands.image.CommandDog;
import net.wuffy.bot.command.commands.image.CommandGarfield;
import net.wuffy.bot.command.commands.image.rra.CommandRRACry;
import net.wuffy.bot.command.commands.image.rra.CommandRRAHug;
import net.wuffy.bot.command.commands.image.rra.CommandRRAKiss;
import net.wuffy.bot.command.commands.image.rra.CommandRRALewd;
import net.wuffy.bot.command.commands.image.rra.CommandRRALick;
import net.wuffy.bot.command.commands.image.rra.CommandRRANom;
import net.wuffy.bot.command.commands.image.rra.CommandRRANyan;
import net.wuffy.bot.command.commands.image.rra.CommandRRAOwo;
import net.wuffy.bot.command.commands.image.rra.CommandRRAPat;
import net.wuffy.bot.command.commands.image.rra.CommandRRAPout;
import net.wuffy.bot.command.commands.image.rra.CommandRRARem;
import net.wuffy.bot.command.commands.image.rra.CommandRRASlap;
import net.wuffy.bot.command.commands.image.rra.CommandRRASmug;
import net.wuffy.bot.command.commands.image.rra.CommandRRAStare;
import net.wuffy.bot.command.commands.image.rra.CommandRRATickle;
import net.wuffy.bot.command.commands.image.rra.CommandRRATriggered;
import net.wuffy.bot.command.commands.information.CommandCommands;
import net.wuffy.bot.command.commands.information.CommandHelp;
import net.wuffy.bot.command.commands.information.CommandInvite;
import net.wuffy.bot.command.commands.information.CommandPing;
import net.wuffy.bot.command.commands.information.CommandShardInfo;
import net.wuffy.bot.command.commands.information.CommandStats;
import net.wuffy.bot.command.commands.information.CommandStatus;
import net.wuffy.bot.command.commands.information.CommandVersion;
import net.wuffy.bot.command.commands.nsfw.CommandE621;
import net.wuffy.bot.command.commands.nsfw.CommandKonachan;
import net.wuffy.bot.command.commands.nsfw.CommandRRAGtn;
import net.wuffy.bot.command.commands.nsfw.CommandRule34;
import net.wuffy.bot.command.commands.nsfw.CommandYandere;
import net.wuffy.bot.command.commands.permission.CommandPermission;
import net.wuffy.bot.command.commands.settings.CommandAutoRole;
import net.wuffy.bot.command.commands.settings.CommandLanguage;
import net.wuffy.bot.command.commands.settings.CommandMention;
import net.wuffy.bot.command.commands.settings.CommandMessage;
import net.wuffy.bot.command.commands.settings.CommandNotification;
import net.wuffy.bot.command.commands.settings.CommandPrefix;
import net.wuffy.bot.command.commands.settings.CommandRename;
import net.wuffy.bot.command.commands.settings.CommandTwitch;
import net.wuffy.bot.command.commands.settings.CommandYoutube;
import net.wuffy.bot.command.commands.utility.CommandGuildInfo;
import net.wuffy.bot.command.commands.utility.CommandUserInfo;
import net.wuffy.core.CoreOLD;

public class CommandRegestry {

	private static final Map<CoreOLD, List<Command>> COMMANDS = new HashMap<CoreOLD, List<Command>>();
	private static final Map<CoreOLD, Map<String, Command>> ENABLED_COMMANDS = new HashMap<CoreOLD, Map<String, Command>>();

	private final CommandHandler handler;

	public CommandRegestry(CommandHandler handler) {
		this.handler = handler;

		if(!COMMANDS.containsKey(this.handler.getCore()))
			CommandRegestry.COMMANDS.put(this.handler.getCore(), new CopyOnWriteArrayList<Command>());

		if(!CommandRegestry.ENABLED_COMMANDS.containsKey(handler.getCore())) {
			CommandRegestry.ENABLED_COMMANDS.put(this.handler.getCore(), new ConcurrentHashMap<String, Command>());

			this.enable(new CommandTest(this.handler));
			this.enable(new CommandMaintaince(this.handler));

			this.enable(new CommandBan(this.handler));
			this.enable(new CommandClear(this.handler));
			this.enable(new CommandHardBan(this.handler));
			this.enable(new CommandKick(this.handler));
			this.enable(new CommandMute(this.handler));
			this.enable(new CommandSoftBan(this.handler));
			this.enable(new CommandUnBan(this.handler));
			this.enable(new CommandUnMute(this.handler));
			this.enable(new CommandVCKick(this.handler));

			this.enable(new CommandHelp(this.handler));
			this.enable(new CommandCommands(this.handler));
			this.enable(new CommandInvite(this.handler));
			this.enable(new CommandPing(this.handler));
			this.enable(new CommandShardInfo(this.handler));
			this.enable(new CommandStats(this.handler));
			this.enable(new CommandStatus(this.handler));
			this.enable(new CommandVersion(this.handler));

			this.enable(new CommandAutoRole(this.handler));
			this.enable(new CommandLanguage(this.handler));
			this.enable(new CommandMention(this.handler));
			this.enable(new CommandMessage(this.handler));
			this.enable(new CommandNotification(this.handler));
			this.enable(new CommandPermission(this.handler));
			this.enable(new CommandPrefix(this.handler));
			this.enable(new CommandRename(this.handler));
			this.enable(new CommandTwitch(this.handler));
			this.enable(new CommandYoutube(this.handler));

			this.enable(new Command8Ball(this.handler));
			this.enable(new CommandAscii(this.handler));
			this.enable(new CommandCoinflip(this.handler));
			this.enable(new CommandLoading(this.handler));
			this.enable(new CommandTableflip(this.handler));

			this.enable(new CommandCat(this.handler));
			this.enable(new CommandDog(this.handler));
			this.enable(new CommandGarfield(this.handler));
			this.enable(new CommandRRACry(this.handler));
			this.enable(new CommandRRAHug(this.handler));
			this.enable(new CommandRRAKiss(this.handler));
			this.enable(new CommandRRALewd(this.handler));
			this.enable(new CommandRRALick(this.handler));
			this.enable(new CommandRRANom(this.handler));
			this.enable(new CommandRRANyan(this.handler));
			this.enable(new CommandRRAOwo(this.handler));
			this.enable(new CommandRRAPat(this.handler));
			this.enable(new CommandRRAPout(this.handler));
			this.enable(new CommandRRARem(this.handler));
			this.enable(new CommandRRASlap(this.handler));
			this.enable(new CommandRRASmug(this.handler));
			this.enable(new CommandRRAStare(this.handler));
			this.enable(new CommandRRATickle(this.handler));
			this.enable(new CommandRRATriggered(this.handler));

			this.enable(new CommandE621(this.handler));
			this.enable(new CommandKonachan(this.handler));
			this.enable(new CommandRRAGtn(this.handler));
			this.enable(new CommandRule34(this.handler));
			this.enable(new CommandYandere(this.handler));

			this.enable(new CommandGuildInfo(this.handler));
			this.enable(new CommandUserInfo(this.handler));
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