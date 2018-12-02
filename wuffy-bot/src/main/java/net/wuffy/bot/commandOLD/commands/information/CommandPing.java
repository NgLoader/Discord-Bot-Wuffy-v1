package net.wuffy.bot.commandOLD.commands.information;

import net.dv8tion.jda.core.Permission;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.bot.lang.I18n;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

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