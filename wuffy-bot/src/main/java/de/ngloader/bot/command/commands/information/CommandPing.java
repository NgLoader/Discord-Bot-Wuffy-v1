package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.Permission;

@CommandSettings(
		category = CommandCategory.INFORMATION,
		guildPermissionRequierd = { Permission.MESSAGE_EMBED_LINKS },
		memberPermissionList = { PermissionKeys.COMMAND_PING },
		memberPermissionRequierd = { PermissionKeys.COMMAND_PING },
		aliases = { "ping", "pong" })
public class CommandPing extends Command {

	public CommandPing(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		Long start = System.currentTimeMillis();

		event.getTextChannel().sendMessage(this.createEmbed(event, MessageType.LOADING).appendDescription(i18n.format(TranslationKeys.MESSAGE_PING_CALCULATING, locale)).build()).queue(success -> 
				this.queue(event, MessageType.INFO, success.editMessage(
						this.createEmbed(event, MessageType.INFO).appendDescription(i18n.format(TranslationKeys.MESSAGE_PING, locale, "%p", Long.toString(System.currentTimeMillis() - start)))
						.build())));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}