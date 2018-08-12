package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

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