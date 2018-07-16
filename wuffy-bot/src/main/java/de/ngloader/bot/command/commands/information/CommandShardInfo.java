package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.TableMessageBuilder;

@Command(aliases = { "shardInfo", "sInfo", "shardI", "clusterinfo", "shards" })
@CommandConfig(category = CommandCategory.INFORMATION)
public class CommandShardInfo extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(member.hasPermission(event.getTextChannel(), "command.shardinfo")) {
			var tableBuilder = new TableMessageBuilder();

			var totalGuilds = 0;
			var totalUsers = 0;
			var totalVoice = 0;

			var worldGuilds = i18n.format(TranslationKeys.MESSAGE_SHARDINFO_GUILDS, locale);
			var worldUsers = i18n.format(TranslationKeys.MESSAGE_SHARDINFO_USERS, locale);
			var worldVoices = i18n.format(TranslationKeys.MESSAGE_SHARDINFO_VOICES, locale);

			tableBuilder
				.addField("", "", "")
				.addField("", worldGuilds, "")
				.addField("", worldUsers, "")
				.addField("", worldVoices, "")
				.nextLine();

			var shardManager = event.getCore().getJdaAdapter(JDAAdapter.class).getShardManager();

			for(int i = 0; i < shardManager.getShardsTotal(); i++) {
				var jda = shardManager.getShardById(i);

				totalGuilds += jda.getGuilds().size();
				totalUsers += jda.getUsers().size();
				totalVoice += jda.getAudioManagers().size();

				tableBuilder
					.addField((event.getJDA().getShardInfo().getShardId() == i ? "* " : ""), Integer.toString(i), " : ")
					.addField(String.format("%s ", worldGuilds.substring(0, 1)), Integer.toString(jda.getGuilds().size()), ", ")
					.addField(String.format("%s ", worldUsers.substring(0, 1)), Integer.toString(jda.getUsers().size()), ", ")
					.addField(String.format("%s ", worldVoices.substring(0, 1)), Integer.toString(jda.getAudioManagers().size()), "")
					.nextLine();
			}

			tableBuilder
				.addField("", i18n.format(TranslationKeys.MESSAGE_SHARDINFO_TOTAL, locale), " : ")
				.addField(String.format("%s ", worldGuilds.substring(0, 1)), Integer.toString(totalGuilds), ", ")
				.addField(String.format("%s ", worldUsers.substring(0, 1)), Integer.toString(totalUsers), ", ")
				.addField(String.format("%s ", worldVoices.substring(0, 1)), Integer.toString(totalVoice), "");

			event.getChannel().sendMessage(String.format("```prolog\n%s\n```", tableBuilder.build())).queue();
		} else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.shardinfo"));
	}
}