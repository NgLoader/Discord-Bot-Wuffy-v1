package de.ngloader.bot.command.commands.utility;

import java.time.Instant;
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

			//TODO check is avaivible

			List<Member> members = guild.getMembers();
			String iconUrl = guild.getIconUrl() == null ? "https://wuffy.eu/pictures/example_avatar_300x300.png" : guild.getIconUrl();

			new ReplayBuilder(event, MessageType.INFO, new EmbedBuilder()
					.setThumbnail(iconUrl)
					.setAuthor(target.getName(), iconUrl)
					.addField("Owner", guild.getOwner().getEffectiveName() + "#" + guild.getOwner().getUser().getDiscriminator(), true)
					.addField("Mitglieder", String.format("%s (da von %s Bots)",
							Integer.toString(members.size()),
							Integer.toString(members.stream().filter(m -> m.getUser().isBot()).collect(Collectors.toList()).size())), true)
					.addField("ID", Long.toString(guild.getIdLong()), true)
					.addField("Region", String.format("%s (%s)", guild.getRegion().getEmoji(), guild.getRegion().getName()), true)
					.addField("Verification-Level", guild.getVerificationLevel().name(), true)
					.addField("Explicit content level", guild.getExplicitContentLevel().getDescription(), true)
					.addField("Text Channels", Integer.toString(guild.getTextChannels().size()), true)
					.addField("Voice Channels", Integer.toString(guild.getVoiceChannels().size()), true)
					.addField("Roles", Integer.toString(guild.getRoles().size()), true)
					.addField("Emojis", Integer.toString(guild.getEmotes().size()), true)
					.setTimestamp(Instant.now()))
			.queue();
		} else if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_GUILDINFO)) {
			
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, member.getLocale(), "%p", PermissionKeys.COMMAND_USERINFO.key));
	}
}