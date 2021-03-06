package net.wuffy.bot.jda;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.wuffy.bot.WuffyBot;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.common.logger.Logger;

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