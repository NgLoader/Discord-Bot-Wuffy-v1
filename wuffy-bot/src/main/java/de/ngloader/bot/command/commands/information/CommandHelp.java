package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "help", "h" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandHelp extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
//		this.replay(event, MessageType.INFO, event.getCore().getI18n().format(TranslationKeys.MESSAGE_HELP, event.getMember(WuffyMember.class).getLocale()));
		event.getAuthor().openPrivateChannel().complete().sendMessage(
				this.buildMessage(MessageType.INFO, event.getCore().getI18n().format(TranslationKeys.MESSAGE_HELP, event.getMember(WuffyMember.class).getLocale())).build()).queue();
	}
}