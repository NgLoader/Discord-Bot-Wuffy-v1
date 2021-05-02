package net.wuffy.bot.command.commands.information;

import java.util.stream.Collectors;

import net.dv8tion.jda.api.Permission;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.jda.JDAAdapter;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
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
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}