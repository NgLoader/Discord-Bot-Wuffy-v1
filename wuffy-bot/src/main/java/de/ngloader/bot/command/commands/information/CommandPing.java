package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "ping", "pong" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandPing extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		Long start = System.currentTimeMillis();
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		String locale = guild.getLocale();

		event.getChannel().sendMessage(event.getCore().getI18n().format(TranslationKeys.MESSAGE_PING_CALCULATING, locale)).complete()
			.editMessage(event.getCore().getI18n().format(TranslationKeys.MESSAGE_PING, locale, "%p", Long.toString(System.currentTimeMillis() - start))).queue();
	}
}