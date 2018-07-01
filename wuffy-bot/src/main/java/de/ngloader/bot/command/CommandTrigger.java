package de.ngloader.bot.command;

import java.util.Arrays;

import de.ngloader.bot.WuffyBot;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.logger.Logger;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandTrigger extends de.ngloader.core.command.CommandTrigger<WuffyBot> {

	public CommandTrigger(CommandManager<WuffyBot> manager) {
		super(manager);
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		var message = event.getMessage().getContentRaw();
		var split = message.split("\\s+");
		var args = Arrays.copyOfRange(split, 1, split.length);
		var command = split[0].toLowerCase();

		var prefixes = Arrays.asList("~", "<@327267953177526273>"); //TODO get prefixes from database and check mention

		for(String prefix : prefixes)
			if(command.startsWith(prefix)) {
				command = command.substring(prefix.length());

				if(command.isEmpty() && args.length > 0) {
					command = args[0].toLowerCase();
					args = Arrays.copyOfRange(args, 1, args.length);
				}

//				var shardGuilds = this.getManager().getCore().getJdaAdapter(JDAAdapter.class).getShardManager().getShards().stream()
//					.map(shard -> "Shard:" + shard.getShardInfo().getShardId() + "\n-  " + shard.getGuilds().stream()
//						.map(guild -> String.format("%s (%s Member)", guild.getName(), Integer.toString(guild.getMembers().size())))
//						.collect(Collectors.joining("\n-  ")))
//					.collect(Collectors.joining("\n\n"));

//				event.getChannel().sendMessage("Command: '" + command + "' args: '" + String.join("', '", args) + "'\n"
//						+ "Shard: " + event.getJDA().getShardInfo().getShardId() + "\n"
//								+ "Total shard count: " + event.getJDA().getShardInfo().getShardTotal() + "\n\n" +
//								shardGuilds).queue(); //TODO remove

				Logger.debug("Command Trigger", "Command: '" + command + "' args: '" + String.join("', '", args) + "'");
				onTrigger(new WuffyMessageRecivedEvent(this.manager.getCore(), event.getJDA(), event.getResponseNumber(), event.getMessage()), command, args);

//				this.manager.getCore().getConfig().sharding.total = 8;
//				this.manager.getCore().getConfig().sharding.shardIds = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
//				this.manager.getCore().getConfig().sharding.total = 2;
//				this.manager.getCore().getConfig().sharding.shardIds = Arrays.asList(0, 1);
//				this.manager.getCore().getJdaAdapter(JDAAdapter.class).restart();
				return;
			}
	}
}