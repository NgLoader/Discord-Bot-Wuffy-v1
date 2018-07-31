package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
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

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_SHARDINFO)) {
			var tableBuilder = new TableMessageBuilder();

			var totalGuilds = 0;
			var totalUsers = 0;
			var totalVoice = 0;

			var worldShards = i18n.format(TranslationKeys.MESSAGE_SHARDINFO_SHARDS, locale);
			var worldGuilds = i18n.format(TranslationKeys.MESSAGE_SHARDINFO_GUILDS, locale);
			var worldUsers = i18n.format(TranslationKeys.MESSAGE_SHARDINFO_USERS, locale);
			var worldVoices = i18n.format(TranslationKeys.MESSAGE_SHARDINFO_VOICES, locale);

			tableBuilder
				.addField("", worldShards, "")
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
					.addField((event.getJDA().getShardInfo().getShardId() == i ? "* " : ""), Integer.toString(i + 1), " : ")
					.addField("", Integer.toString(jda.getGuilds().size()), ", ")
					.addField("", Integer.toString(jda.getUsers().size()), ", ")
					.addField("", Integer.toString(jda.getAudioManagers().size()), "")
					.nextLine();
			}

			tableBuilder
				.addField("", i18n.format(TranslationKeys.MESSAGE_SHARDINFO_TOTAL, locale), " : ")
				.addField("", Integer.toString(totalGuilds), ", ")
				.addField("", Integer.toString(totalUsers), ", ")
				.addField("", Integer.toString(totalVoice), "");

			event.getChannel().sendMessage(String.format("```prolog\n%s\n```", tableBuilder.build())).queue();
//			this.replay(event, MessageType.LIST, String.format("\n%s\n", tableBuilder.build()));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_SHARDINFO.key));
	}
}