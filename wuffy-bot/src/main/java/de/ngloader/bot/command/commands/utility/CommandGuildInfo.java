package de.ngloader.bot.command.commands.utility;

import java.util.List;
import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.bot.util.ReplayBuilder;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

@Command(aliases = { "guildinfo", "ginfo", "infog", "guildi", "iguild" })
@CommandConfig(category = CommandCategory.UTILITY)
public class CommandGuildInfo extends BotCommand {

	/*
	 * ~guildInfo //Currently guild (PERMISSION)
	 * ~guildInfo <guildID> //ADMIN
	 */

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();

		if(args.length > 0 && event.getCore().isAdmin(event.getAuthor())) {
			Guild target = event.getJDA().getGuildById(Long.parseLong(args[0]));

			if(target != null) {
				if(target.isAvailable()) {
					new ReplayBuilder(event, MessageType.INFO, getGuildInfo(target, i18n, member.getLocale())).queue();
				} else
					this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_GUILDINFO_NOT_AVAIVIBLE, member.getLocale(),
							"%n", args[0]));
			} else
				this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_GUILDINFO_NOT_FOUND, member.getLocale(),
						"%n", args[0]));
		} else if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_GUILDINFO)) {
			new ReplayBuilder(event, MessageType.INFO, getGuildInfo(guild, i18n, member.getLocale())).queue();
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, member.getLocale(), "%p", PermissionKeys.COMMAND_USERINFO.key));
	}

	private EmbedBuilder getGuildInfo(Guild guild, I18n i18n, String locale) {
		List<Member> members = guild.getMembers();
		String iconUrl = guild.getIconUrl() == null ? "https://wuffy.eu/pictures/example_avatar_300x300.png" : guild.getIconUrl();

		return new EmbedBuilder()
				.setThumbnail(iconUrl)
				.setAuthor(guild.getName(), iconUrl)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_OWNER, locale), guild.getOwner().getEffectiveName() + "#" + guild.getOwner().getUser().getDiscriminator(), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_MEMBERS, locale), i18n.format(TranslationKeys.MESSAGE_GUILDINFO_MEMBERS_ANSWER, locale,
						"%m", Integer.toString(members.size()),
						"%b", Integer.toString(members.stream().filter(m -> m.getUser().isBot()).collect(Collectors.toList()).size())), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_ID, locale), Long.toString(guild.getIdLong()), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_REGION, locale), String.format("%s (%s)", guild.getRegion().getEmoji(), guild.getRegion().getName()), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_VERIFICATION_LEVEL, locale),
						i18n.format(String.format("%s_%s", TranslationKeys.MESSAGE_GUILDINFO_VERIFICATION_LEVEL, guild.getExplicitContentLevel().getKey()), locale), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_EXPLICIT_CONTENT_LEVEL, locale),
						i18n.format(String.format("%s_%s", TranslationKeys.MESSAGE_GUILDINFO_EXPLICIT_CONTENT_LEVEL, guild.getExplicitContentLevel().getKey()), locale), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_REQUIRED_MFA_LEVEL, locale),
						i18n.format(String.format("%s_%s", TranslationKeys.MESSAGE_GUILDINFO_REQUIRED_MFA_LEVEL, guild.getRequiredMFALevel().getKey()), locale), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_DEFAULT_NOTIFICATION_LEVEL, locale),
						i18n.format(String.format("%s_%s", TranslationKeys.MESSAGE_GUILDINFO_DEFAULT_NOTIFICATION_LEVEL, guild.getDefaultNotificationLevel().getKey()), locale), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_TEXT_CHANNELS, locale), Integer.toString(guild.getTextChannels().size()), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_VOICE_CHANNELS, locale), Integer.toString(guild.getVoiceChannels().size()), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_ROLES, locale), Integer.toString(guild.getRoles().size()), true)
				.addField(i18n.format(TranslationKeys.MESSAGE_GUILDINFO_EMOJIS, locale), Integer.toString(guild.getEmotes().size()), true)
				.setTimestamp(guild.getCreationTime());
	}
}