package de.ngloader.api;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;

public interface IShardProvider {

	public JDA getJDA();

	public ShardManager getShardManager();
}