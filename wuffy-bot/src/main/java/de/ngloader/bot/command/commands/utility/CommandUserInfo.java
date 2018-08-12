package de.ngloader.bot.command.commands.utility;

import java.time.format.DateTimeFormatterBuilder;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

@CommandSettings(
		category = CommandCategory.UTILITY,
		guildPermissionRequierd = { Permission.MESSAGE_EMBED_LINKS },
		memberPermissionList = { PermissionKeys.COMMAND_USERINFO },
		memberPermissionRequierd = { PermissionKeys.COMMAND_USERINFO },
		aliases = { "userinfo", "useri", "uinfo" })
public class CommandUserInfo extends Command {

	public CommandUserInfo(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		String locale = member.getLocale();

		if(args.length > 0) {
			Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args[0]);

			if(memberSelected != null) {
				this.queue(event, MessageType.INFO, event.getTextChannel().sendMessage(this.createEmbed(event, MessageType.INFO)
						.setAuthor(memberSelected.getUser().getName(), memberSelected.getUser().getEffectiveAvatarUrl(), memberSelected.getUser().getEffectiveAvatarUrl())
						.setThumbnail(memberSelected.getUser().getEffectiveAvatarUrl())

						.addField(i18n.format(TranslationKeys.MESSAGE_USERINFO_USERNAME, locale), memberSelected.getUser().getName(), true)
						.addField(i18n.format(TranslationKeys.MESSAGE_USERINFO_DISCRIMINATOR, locale), memberSelected.getUser().getDiscriminator(), true)
						.addField(i18n.format(TranslationKeys.MESSAGE_USERINFO_ID, locale), memberSelected.getUser().getId(), true)
						.addField(i18n.format(TranslationKeys.MESSAGE_USERINFO_ROLES, locale), Integer.toString(memberSelected.getRoles().size()), true)
						.addField(i18n.format(TranslationKeys.MESSAGE_USERINFO_USERLOCALE, locale), member.getUser(WuffyUser.class).getUserLocale() != null ?
								member.getUser(WuffyUser.class).getUserLocale() :
								i18n.format(TranslationKeys.MESSAGE_USERINFO_USERLOCALE_NOT_SELECTED, locale), true)
						.addField(i18n.format(TranslationKeys.MESSAGE_USERINFO_GAME, locale), memberSelected.getGame() != null ?
								memberSelected.getGame().getName() :
								i18n.format(TranslationKeys.MESSAGE_USERINFO_GAME_NOTHING_PLAYING, locale), true)
						.addField(
								i18n.format(TranslationKeys.MESSAGE_USERINFO_STATUS, locale),
								i18n.format("message_online_status_" + memberSelected.getOnlineStatus().name().toLowerCase(), locale), true)
						.addField(
								i18n.format(TranslationKeys.MESSAGE_USERINFO_CREATION, locale),
								memberSelected.getUser().getCreationTime().format(new DateTimeFormatterBuilder()
										.appendPattern(i18n.format(TranslationKeys.MESSAGE_DATE_FORMAT, locale))
										.toFormatter()),
								true)
						.build()));
			} else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[0]));
		} else
			this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_USERINFO_SYNTAX, locale));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}