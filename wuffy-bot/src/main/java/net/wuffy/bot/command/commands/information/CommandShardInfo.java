package net.wuffy.bot.command.commands.information;

import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.jda.JDAAdapter;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.lang.I18n;
import net.wuffy.core.util.ArgumentBuffer;
import net.wuffy.core.util.TableMessageBuilder;

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
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
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}