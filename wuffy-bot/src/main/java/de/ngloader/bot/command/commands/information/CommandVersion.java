package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;

@Command(aliases = { "version", "ver" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandVersion extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(member.hasPermission(event.getTextChannel(), "command.version"))
			this.replay(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_VERSION, locale, "%v", event.getCore().getConfig().instanceVersion));
		else
			this.replay(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.version"));
	}
}