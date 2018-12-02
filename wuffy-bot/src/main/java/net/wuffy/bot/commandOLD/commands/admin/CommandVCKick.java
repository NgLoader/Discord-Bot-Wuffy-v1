package net.wuffy.bot.commandOLD.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
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