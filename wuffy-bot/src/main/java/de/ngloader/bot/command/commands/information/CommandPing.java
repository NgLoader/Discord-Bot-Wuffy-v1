package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;

@Command(aliases = { "ping", "pong" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandPing extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_PING)) {
			Long start = System.currentTimeMillis();

			event.getChannel().sendMessage(this.buildMessage(MessageType.LOADING, i18n.format(TranslationKeys.MESSAGE_PING_CALCULATING, locale)).build()).complete()
				.editMessage(this.buildMessage(MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_PING, locale, "%p", Long.toString(System.currentTimeMillis() - start))).build()).queue();
		} else
			this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_PING.key));
	}
}