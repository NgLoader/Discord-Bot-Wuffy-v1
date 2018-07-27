package de.ngloader.bot;

import de.ngloader.core.CoreConfig;
import de.ngloader.core.config.Config;

@Config(path = "./wuffy/bot.json", sourcePath = "/config/bot.json")
public class BotConfig extends CoreConfig {

	public String mentionId;

	public String twitchId;
	public String twithchAppSecret;

	public ShardConfig sharding;
}