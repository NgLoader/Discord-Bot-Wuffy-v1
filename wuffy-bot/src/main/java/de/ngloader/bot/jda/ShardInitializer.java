package de.ngloader.bot.jda;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.command.CommandHandler;
import de.ngloader.common.logger.Logger;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ShardInitializer extends ListenerAdapter {

	private WuffyBot core;

	public ShardInitializer(WuffyBot core) {
		this.core = core;

		this.core.getJdaAdapter().addListener(this);
	}

	@Override
	public void onReady(ReadyEvent event) {
		Logger.info("ShardInitializer", String.format("Initializing shard \"%s\".", Integer.toString(event.getJDA().getShardInfo().getShardId())));

		new CommandHandler(this.core, event.getJDA());

		Logger.info("ShardInitializer", String.format("Initialized shard \"%s\".", Integer.toString(event.getJDA().getShardInfo().getShardId())));
	}

	public WuffyBot getCore() {
		return this.core;
	}
}