package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "invite" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandInvite extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
//		event.getChannel().sendMessage(event.getCore().getI18n().format(TranslationKeys.MESSAGE_INVITE, event.getMember(WuffyMember.class).getLocale())).queue();
		event.getAuthor().openPrivateChannel().complete().sendMessage(
				this.buildMessage(MessageType.INFO, event.getCore().getI18n().format(TranslationKeys.MESSAGE_INVITE, event.getMember(WuffyMember.class).getLocale())).build()).queue();
	}
}