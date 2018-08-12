package de.ngloader.bot.command.commands.admin;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

@CommandSettings(
		category = CommandCategory.ADMIN,
		guildPermissionRequierd = { Permission.MANAGE_CHANNEL, Permission.VOICE_MOVE_OTHERS },
		memberPermissionList = { PermissionKeys.COMMAND_VCKICK },
		memberPermissionRequierd = { PermissionKeys.COMMAND_VCKICK },
		aliases = { "vckick", "voicekick", "voicek" })
public class CommandVCKick extends Command {

	public CommandVCKick(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		String locale = member.getLocale();

		Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args[0]);

		if(args.length > 0) {
			if(memberSelected != null) {
				if(memberSelected.getVoiceState().inVoiceChannel()) {
					if(guild.hasHighestRole(member, memberSelected)) {
						if(guild.getSelfMember().canInteract(memberSelected)) {
							VoiceChannel before = memberSelected.getVoiceState().getChannel();
							Channel channel = event.getGuild().getController().createVoiceChannel("wuffy-vckick").complete();

							event.getGuild().getController().moveVoiceMember(memberSelected, channel.getGuild().getVoiceChannelById(channel.getIdLong())).complete();

							channel.delete().queue();

							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_VCKICK_KICKED, locale, "%m", memberSelected.getEffectiveName(), "%c", before.getName()));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_BOT_NO_INTERACT, locale, "%m", memberSelected.getEffectiveName()));
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
				} else
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOT_IN_VOICE_TARGET, locale, "%m", memberSelected.getEffectiveName()));
			} else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[0]));
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}