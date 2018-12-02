package net.wuffy.bot.commandOLD.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.DiscordUtil;

@CommandSettings(
		category = CommandCategory.ADMIN,
		guildPermissionRequierd = { Permission.VOICE_DEAF_OTHERS },
		memberPermissionList = { PermissionKeys.COMMAND_UNMUTE },
		memberPermissionRequierd = { PermissionKeys.COMMAND_UNMUTE },
		aliases = { "unmute" })
public class CommandUnMute extends Command {

	public CommandUnMute(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		String locale = member.getLocale();

		if(args.length > 0) {
			Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args[0]);

			if(memberSelected != null) {
				if(!guild.getSelfMember().canInteract(memberSelected)) {
					if(guild.hasHighestRole(member, memberSelected))
						if(memberSelected.getVoiceState().isGuildMuted()) {
							event.getGuild().getController().setMute(memberSelected, false).queue();

							//TODO change mute history to unmuted

							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_UNMUTE_UNMUTED, locale, "%m", memberSelected.getEffectiveName()));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_UNMUTE_ALREADY_UNMUTED, locale, "%m", memberSelected.getEffectiveName()));
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
				} else
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_BOT_NO_INTERACT, locale, "%m", memberSelected.getEffectiveName()));
			} else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[0]));
			//Member not found
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}