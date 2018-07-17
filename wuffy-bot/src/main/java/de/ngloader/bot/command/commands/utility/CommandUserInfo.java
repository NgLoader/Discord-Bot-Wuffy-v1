package de.ngloader.bot.command.commands.utility;

import java.time.format.DateTimeFormatterBuilder;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

@Command(aliases = { "userinfo", "infou", "uinfo", "useri", "iuser" })
@CommandConfig(category = CommandCategory.UTILITY)
public class CommandUserInfo extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), "command.userinfo"))
			if(args.length > 0) {
				Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args[0]);

				if(memberSelected != null) {
					event.getMessage().delete().queue();

					event.getChannel().sendMessage(new EmbedBuilder()
							.setColor(memberSelected.getColorRaw())

							.setTitle(memberSelected.getUser().getName(), memberSelected.getUser().getEffectiveAvatarUrl())
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
									i18n.format("message_userinfo_status_" + memberSelected.getOnlineStatus().name().toLowerCase(), locale), true)

							.setFooter(
									memberSelected.getUser().getCreationTime().format(new DateTimeFormatterBuilder()
											.appendPattern(i18n.format(TranslationKeys.MESSAGE_USERINFO_DATE_FORMAT, locale))
											.toFormatter()),
									memberSelected.getUser().getEffectiveAvatarUrl())
							.build()).queue();
				} else
					this.replay(event, i18n.format(TranslationKeys.MESSAGE_USERINFO_MEMBER_NOT_FOUND, locale, "%m", args[0]));
				//Member not found
			} else
				this.replay(event, i18n.format(TranslationKeys.MESSAGE_USERINFO_FALSE_ARGS, locale));
			//No args
		else
			this.replay(event, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.userinfo"));
	}
}