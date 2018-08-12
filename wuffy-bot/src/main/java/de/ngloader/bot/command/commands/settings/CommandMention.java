package de.ngloader.bot.command.commands.settings;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.SETTINGS,
		memberPermissionList = { PermissionKeys.COMMAND_MENTION },
		memberPermissionRequierd = { PermissionKeys.COMMAND_MENTION },
		aliases = { "mention" })
public class CommandMention extends Command {

	public CommandMention(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		String locale = member.getLocale();

		Boolean mention = !guild.isMention();

		guild.setMention(mention);

		if(mention)
			this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MENTION_ENABLE, locale));
		else
			this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MENTION_DISABLE, locale));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}