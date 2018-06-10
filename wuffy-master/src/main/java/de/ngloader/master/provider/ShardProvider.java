package de.ngloader.master.provider;

import de.ngloader.api.IShardProvider;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;

public class ShardProvider implements IShardProvider {

	private ShardManager shardManager;

	public ShardProvider() {
		
	}

	@Override
	public JDA getJDA() {
		return this.shardManager.getApplicationInfo().getJDA();
	}
}