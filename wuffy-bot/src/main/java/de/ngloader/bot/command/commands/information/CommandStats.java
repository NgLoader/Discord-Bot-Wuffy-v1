package de.ngloader.bot.command.commands.information;

import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(aliases = { "stats" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandStats extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(member.hasPermission(event.getTextChannel(), "command.stats")) {
			var embedBuilder = new EmbedBuilder()
					.addField(i18n.format(TranslationKeys.MESSAGE_STATS_VERSION, locale), event.getCore().getConfig().instanceVersion, true)
					.addField(i18n.format(TranslationKeys.MESSAGE_STATS_LIBRARY, locale), "JDA", false)
					.addField(i18n.format(TranslationKeys.MESSAGE_STATS_OWNER, locale), "Nils#0001", true)
					.addField(i18n.format(TranslationKeys.MESSAGE_STATS_ADMINS, locale), event.getCore().getConfig().admins.isEmpty() ? "-" : event.getCore().getConfig().admins.stream()
							.map(id -> String.format("<@%s>", id))
							.collect(Collectors.joining("\n")), false)
					.addField(i18n.format(TranslationKeys.MESSAGE_STATS_GUILDS, locale), Integer.toString(event.getCore().getJdaAdapter(JDAAdapter.class).getShardManager().getGuilds().size()), true)
					.addField(i18n.format(TranslationKeys.MESSAGE_STATS_USERS, locale), Integer.toString(event.getCore().getJdaAdapter(JDAAdapter.class).getShardManager().getUsers().size()), true)
					.addField(i18n.format(TranslationKeys.MESSAGE_STATS_VOICES, locale), "Unkown", true);

			event.getChannel().sendMessage(embedBuilder.build()).queue();
		} else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.stats"));
	}
}