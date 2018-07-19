package de.ngloader.bot.command.commands.moderator;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.DiscordUtil;
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
				Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args[0]);

				if(memberSelected != null) {
					if(!guild.getSelfMember().canInteract(memberSelected)) {
						if(guild.hasHighestRole(member, memberSelected))
							if(memberSelected.getVoiceState().isGuildMuted()) {
								event.getGuild().getController().setMute(memberSelected, false).queue();

								//TODO change mute history to unmuted

								this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_UNMUTE_UNMUTED, locale, "%m", memberSelected.getEffectiveName()));
							} else
								this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_UNMUTE_ALREADY_UNMUTED, locale, "%m", memberSelected.getEffectiveName()));
						this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
					} else
						this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_BOT_NO_INTERACT, locale, "%m", memberSelected.getEffectiveName()));
				} else
					this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[0]));
				//Member not found
			} else
				this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_UNMUTE_SYNTAX, locale));
			//No args
		else
			this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.unmute"));
	}
}