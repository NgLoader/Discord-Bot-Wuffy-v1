package de.ngloader.bot.command.commands;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.jda.JDAAdapter;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.TableMessageBuilder;

@Command(aliases = { "shardInfo", "sInfo", "shardI" })
public class CommandShardInfo implements BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		var tableBuilder = new TableMessageBuilder();

		var totalGuilds = 0;
		var totalUsers = 0;
		var totalVoice = 0;

		tableBuilder
			.addField("", "", "")
			.addField("", "Guilds \\/", "")
			.addField("", "Users \\/", "")
			.addField("", "Voices \\/", "")
			.nextLine();

		var shardManager = event.getCore().getJdaAdapter(JDAAdapter.class).getShardManager();
		for(int i = 0; i < shardManager.getShardsTotal(); i++) {
			var jda = shardManager.getShardById(i);

			totalGuilds += jda.getGuilds().size();
			totalUsers += jda.getUsers().size();
			totalVoice += jda.getAudioManagers().size();

			tableBuilder
				.addField((event.getJDA().getShardInfo().getShardId() == i ? "* " : ""), Integer.toString(i), " : ")
				.addField("G ", Integer.toString(jda.getGuilds().size()), ", ")
				.addField("U ", Integer.toString(jda.getUsers().size()), ", ")
				.addField("V ", Integer.toString(jda.getAudioManagers().size()), "")
				.nextLine();
		}

		tableBuilder
			.addField("", "T", " : ")
			.addField("G ", Integer.toString(totalGuilds), ", ")
			.addField("U ", Integer.toString(totalUsers), ", ")
			.addField("V ", Integer.toString(totalVoice), "");

		event.getChannel().sendMessage(String.format("```prolog\n%s\n```", tableBuilder.build())).queue();
	}
}