package net.wuffy.bot.command.commands.utility;

import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.lang.I18n;
import net.wuffy.core.util.ArgumentBuffer;

@CommandSettings(
		category = CommandCategory.UTILITY,
		guildPermissionRequierd = { Permission.MESSAGE_EMBED_LINKS },
		memberPermissionList = { PermissionKeys.COMMAND_GUILDINFO },
		memberPermissionRequierd = { PermissionKeys.COMMAND_GUILDINFO },
		aliases = { "guildinfo", "guildi"," ginfo" })
public class CommandGuildInfo extends Command {

	/*
	 * ~guildInfo //Currently guild (PERMISSION)
	 * ~guildInfo <guildID> //ADMIN
	 */

	public CommandGuildInfo(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);

		if(args.getSize() > 0 && event.getCore().isAdmin(event.getAuthor())) {
			Guild target = event.getJDA().getGuildById(Long.parseLong(args.get(0)));

			if(target != null) {
				if(target.isAvailable()) {
					this.queue(event, MessageType.INFO, event.getTextChannel().sendMessage(getGuildInfo(event, guild, i18n, member.getLocale()).build()));
				} else
					this.sendMessage(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_GUILDINFO_NOT_AVAIVIBLE, member.getLocale(),
							"%n", args.get(0)));
			} else
				this.sendMessage(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_GUILDINFO_NOT_FOUND, member.getLocale(),
						"%n", args.get(0)));
		} else {
			this.queue(event, MessageType.INFO, event.getTextChannel().sendMessage(getGuildInfo(event, guild, i18n, member.getLocale()).build()));
		}
	}

	private EmbedBuilder getGuildInfo(WuffyMessageRecivedEvent event, Guild guild, I18n i18n, String locale) {
		List<Member> members = guild.getMembers();
		String iconUrl = guild.getIconUrl() == null ? "https://wuffy.eu/pictures/example_avatar_300x300.png" : guild.getIconUrl();

		return this.createEmbed(event, MessageType.INFO)
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
				/*.setTimestamp(guild.getCreationTime())*/;
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}