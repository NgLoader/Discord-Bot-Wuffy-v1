package net.wuffy.bot.command.commands.information;

import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.INFORMATION,
		memberPermissionList = { PermissionKeys.COMMAND_VERSION },
		memberPermissionRequierd = { PermissionKeys.COMMAND_VERSION },
		aliases = { "version", "ver" },
		privateChatCommand = true)
public class CommandVersion extends Command {

	public CommandVersion(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendMessage(event, MessageType.INFO, this.i18n.format(TranslationKeys.MESSAGE_VERSION, event.getMember(WuffyMember.class).getLocale(), "%v", event.getCore().getConfig().instanceVersion));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		String locale = event.getAuthor(WuffyUser.class).getUserLocale();

		if(locale == null)
			locale = "en-US";

		this.sendMessage(event, MessageType.INFO, this.i18n.format(TranslationKeys.MESSAGE_VERSION, locale, "%v", event.getCore().getConfig().instanceVersion));
	}
}