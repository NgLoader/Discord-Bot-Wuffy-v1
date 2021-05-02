package net.wuffy.bot;

import net.wuffy.common.config.Config;
import net.wuffy.core.CoreConfig;

@Config(path = "./wuffy/bot.json", sourcePath = "/config/bot.json")
public class BotConfig extends CoreConfig {

	public String youtubeToken = "";
	public ShardConfig sharding = new ShardConfig();

	public class ShardConfig {
		public boolean enabled = false;
		public int[] shardIds = new int[] { 0, 1 };
		public int total = 2;
	}
}