package de.ngloader.bot.command.commands.moderator;

import java.util.Arrays;
import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.BanInfo;
import de.ngloader.bot.database.BanInfo.BanType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.entities.Member;

@Command(aliases = { "hardban", "hban" })
@CommandConfig(category = CommandCategory.MODERATOR)
public class CommandHardBan extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), "command.hardban"))
			if(args.length > 0) {
				if(args[0].matches("<@[0-9]{14,20}>")) {
					Member memberSelected = event.getGuild().getMemberById(args[0].substring(2, args[0].length() - 1));

					if(memberSelected != null) {
						if(!memberSelected.isOwner()) {
							if(guild.hasHighestRole(member, memberSelected))
								if(guild.getSelfMember().canInteract(memberSelected)) {
									var reason = args.length > 1 ? Arrays.asList(Arrays.copyOfRange(args, 1, args.length)).stream().collect(Collectors.joining(" ")) : "";

									event.getMessage().delete().queue();

									guild.setBan(memberSelected.getUser().getIdLong(), new BanInfo(
											BanType.HARD,
											memberSelected.getUser().getIdLong(),
											member.getUser().getIdLong(),
											System.currentTimeMillis(),
											System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 14),
											reason));

									if(reason.isEmpty()) {
										event.getGuild().getController().ban(memberSelected, 14).queue();
										this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_HARDBAN, locale, "%m"));
									} else {
										event.getGuild().getController().ban(memberSelected, 14, reason).queue();
										this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_HARDBAN_REASON, locale,
												"%m", memberSelected.getEffectiveName(),
												"%d", Integer.toString(14),
												"%r", reason));
									}
								} else
									this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_BOT_NO_INTERACT, locale, "%m", memberSelected.getEffectiveName()));
							else
								this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_HARDBAN_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
						} else
							this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NOT_ALLOWED_BY_GUILD_OWNER, locale));
					} else
						this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_HARDBAN_MEMBER_NOT_FOUND, locale, "%m", args[0].substring(2, args[0].length() - 1)));
					//Member not found
				} else
					this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_HARDBAN_USER_CAN_NOT_EXIST, locale));
				//No user
			} else
				this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_HARDBAN_FALSE_ARGS, locale));
			//No args
		else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.hardban"));
	}
}