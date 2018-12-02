package net.wuffy.bot.commandOLD.commands.botauthor;

import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.BOT_AUTHOR,
		guildPermissionRequierd = { },
		memberPermissionRequierd = { },
		memberPermissionList = { },
		aliases = { "maintaince", "maint", "mtaince" },
		adminCommand  = true,
		alpha = true)
public class CommandMaintaince extends Command {

	public CommandMaintaince(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendMessage(event, MessageType.SUCCESS, "MAINTAINCE");
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}