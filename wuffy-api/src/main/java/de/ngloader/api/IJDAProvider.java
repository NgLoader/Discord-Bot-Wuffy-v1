package de.ngloader.api;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;

public interface IJDAProvider {

	public JDA getJDA(int shardId);

	public ShardManager getShardManager();
}