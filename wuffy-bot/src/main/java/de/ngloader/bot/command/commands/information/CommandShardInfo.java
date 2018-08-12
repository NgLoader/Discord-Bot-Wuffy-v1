package de.ngloader.bot.command.commands.information;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.TableMessageBuilder;

@CommandSettings(
		category = CommandCategory.INFORMATION,
		memberPermissionList = { PermissionKeys.COMMAND_SHARDINFO },
		memberPermissionRequierd = { PermissionKeys.COMMAND_SHARDINFO },
		aliases = { "shardinfo", "shardi", "shards" })
public class CommandShardInfo extends Command {

	public CommandShardInfo(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getGuild(WuffyGuild.class).isMessageDeleteExecuter())
			event.getMessage().delete().queue();

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

		this.queue(event, MessageType.LIST, event.getChannel().sendMessage(String.format("```prolog\n%s\n```", tableBuilder.build())));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}