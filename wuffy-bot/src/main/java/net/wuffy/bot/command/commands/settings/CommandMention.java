package net.wuffy.bot.command.commands.settings;

import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
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
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}