package de.ngloader.bot.command.commands.information;

import java.util.stream.Collectors;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.Permission;

@CommandSettings(
		category = CommandCategory.INFORMATION,
		guildPermissionRequierd = { Permission.MESSAGE_EMBED_LINKS },
		memberPermissionList = { PermissionKeys.COMMAND_STATS },
		memberPermissionRequierd = { PermissionKeys.COMMAND_STATS },
		aliases = { "stats" })
public class CommandStats extends Command {

	public CommandStats(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		String locale = event.getMember(WuffyMember.class).getLocale();

		this.queue(event, MessageType.INFO, event.getTextChannel().sendMessage(this.createEmbed(event, MessageType.INFO)
				.addField(this.i18n.format(TranslationKeys.MESSAGE_STATS_VERSION, locale), event.getCore().getConfig().instanceVersion, true)
				.addField(this.i18n.format(TranslationKeys.MESSAGE_STATS_LIBRARY, locale), "JDA", true)
				.addField(this.i18n.format(TranslationKeys.MESSAGE_STATS_OWNER, locale), "Nils#0001", true)
				.addField(this.i18n.format(TranslationKeys.MESSAGE_STATS_ADMINS, locale), event.getCore().getConfig().admins.isEmpty() ? "-" : event.getCore().getConfig().admins.stream()
						.map(id -> String.format("<@%s>", id))
						.collect(Collectors.joining("\n")), true)
				.addField(this.i18n.format(TranslationKeys.MESSAGE_STATS_GUILDS, locale), Integer.toString(event.getCore().getJdaAdapter(JDAAdapter.class).getShardManager().getGuilds().size()), true)
				.addField(this.i18n.format(TranslationKeys.MESSAGE_STATS_USERS, locale), Integer.toString(event.getCore().getJdaAdapter(JDAAdapter.class).getShardManager().getUsers().size()), true)
				.addField(this.i18n.format(TranslationKeys.MESSAGE_STATS_VOICES, locale), "Unkown", true).build()));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}