package net.wuffy.bot;

import net.wuffy.common.config.Config;
import net.wuffy.core.CoreConfig;

@Config(path = "./wuffy/bot.json", sourcePath = "/config/bot.json")
public class BotConfig extends CoreConfig {

	public String mentionId;

	public String twitchId;
	public String twithchAppSecret;

	public String youtubeToken;

	public ShardConfig sharding;
}