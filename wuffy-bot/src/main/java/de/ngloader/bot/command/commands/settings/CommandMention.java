package de.ngloader.bot.command.commands.settings;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;

@Command(aliases = { "mention" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandMention extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), "command.mention")) {
			Boolean mention = !guild.isMention();

			guild.setMention(mention);

			if(mention)
				this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MENTION_ENABLE, locale));
			else
				this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MENTION_DISABLE, locale));
		} else
			this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.mention"));
	}
}