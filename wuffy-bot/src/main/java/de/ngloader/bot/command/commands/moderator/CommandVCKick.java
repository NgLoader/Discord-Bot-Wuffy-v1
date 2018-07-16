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
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

@Command(aliases = { "vckick", "voicekick", "voiceckick", "voicechatkick" })
@CommandConfig(category = CommandCategory.MODERATOR)
public class CommandVCKick extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), "command.vckick"))
			if(args.length > 0) {
				if(args[0].matches("<@[0-9]{14,20}>")) {
					Member memberSelected = event.getGuild().getMemberById(args[0].substring(2, args[0].length() - 1));

					if(memberSelected != null) {
						if(memberSelected.getVoiceState().inVoiceChannel()) {
							if(guild.hasHighestRole(member, memberSelected)) {
								if(guild.getSelfMember().canInteract(memberSelected)) {
									VoiceChannel before = memberSelected.getVoiceState().getChannel();
									Channel channel = event.getGuild().getController().createVoiceChannel("wuffy-vckick").complete();

									event.getGuild().getController().moveVoiceMember(memberSelected, channel.getGuild().getVoiceChannelById(channel.getIdLong())).complete();

									channel.delete().queue();

									this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_VCKICK_KICKED, locale, "%m", memberSelected.getEffectiveName(), "%c", before.getName()));
								} else
									this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_BOT_NO_INTERACT, locale, "%m", memberSelected.getEffectiveName()));
							} else
								this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_VCKICK_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
						} else
							this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_VCKICK_NOT_IN_VOICE, locale, "%m", memberSelected.getEffectiveName()));
						//Member not in voice
					} else
						this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_VCKICK_MEMBER_NOT_FOUND, locale, "%m", args[0].substring(2, args[0].length() - 1)));
					//Member not found
				} else
					this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_VCKICK_USER_CAN_NOT_EXIST, locale));
				//No user
			} else
				this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_VCKICK_FALSE_ARGS, locale));
			//No args
		else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.vckick"));
	}
}