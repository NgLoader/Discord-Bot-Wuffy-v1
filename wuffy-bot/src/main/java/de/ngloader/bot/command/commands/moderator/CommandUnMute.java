package de.ngloader.bot.command.commands.moderator;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.entities.Member;

@Command(aliases = { "unmute", "umute" })
@CommandConfig(category = CommandCategory.MODERATOR)
public class CommandUnMute extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		event.getMessage().delete().queue();

		if(member.hasPermission(event.getTextChannel(), "command.unmute"))
			if(args.length > 0) {
				if(args[0].matches("<@[0-9]{14,20}>")) {
					Member memberSelected = event.getGuild().getMemberById(args[0].substring(2, args[0].length() - 1));

					if(memberSelected != null) {
						event.getMessage().delete().queue();

						if(!memberSelected.isOwner()) {
							if(guild.hasHighestRole(member, memberSelected))
								if(memberSelected.getVoiceState().isGuildMuted()) {
									event.getGuild().getController().setMute(memberSelected, false).queue();
									this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_UNMUTE_UNMUTED, locale, "%m", memberSelected.getEffectiveName()));
								} else
									this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_UNMUTE_ALREADY_UNMUTED, locale, "%m", memberSelected.getEffectiveName()));
							this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_UNMUTE_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
						} else
							this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NOT_ALLOWED_BY_GUILD_OWNER, locale));
					} else
						this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_UNMUTE_MEMBER_NOT_FOUND, locale, "%m", args[0].substring(2, args[0].length() - 1)));
					//Member not found
				} else
					this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_UNMUTE_USER_CAN_NOT_EXIST, locale));
				//No user
			} else
				this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_UNMUTE_FALSE_ARGS, locale));
			//No args
		else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.unmute"));
	}
}