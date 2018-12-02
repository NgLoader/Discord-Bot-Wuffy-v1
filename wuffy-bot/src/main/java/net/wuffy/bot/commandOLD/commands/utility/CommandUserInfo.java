package net.wuffy.bot.commandOLD.commands.utility;

import java.time.format.DateTimeFormatterBuilder;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.DiscordUtil;

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
		event.getTextChannel()

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
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}