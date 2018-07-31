package de.ngloader.bot.command.commands.information;

import java.util.concurrent.TimeUnit;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

@Command(aliases = { "ping", "pong" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandPing extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_PING)) {
			if(event.getGuild(WuffyGuild.class).isMessageDeleteExecuter())
				event.getMessage().delete().queue();

			Long start = System.currentTimeMillis();

			MessageAction action = event.getChannel().sendMessage(this.buildMessage(MessageType.LOADING, i18n.format(TranslationKeys.MESSAGE_PING_CALCULATING, locale)).build()).complete()
				.editMessage(this.buildMessage(MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_PING, locale, "%p", Long.toString(System.currentTimeMillis() - start))).build());

			if(event.getGuild(WuffyGuild.class).isMessageDeleteBot() && event.getGuild(WuffyGuild.class).isMessageDeleteDelay(MessageType.INFO))
				action.queue(success -> success.delete().queueAfter(event.getGuild(WuffyGuild.class).getMessageDeleteDelay(MessageType.INFO), TimeUnit.SECONDS));
			else
				action.queue();
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_PING.key));
	}
}