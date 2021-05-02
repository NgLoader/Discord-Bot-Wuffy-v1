package net.wuffy.bot.command.commands.botauthor;

import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		this.sendMessage(event, MessageType.SUCCESS, "MAINTAINCE");
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}