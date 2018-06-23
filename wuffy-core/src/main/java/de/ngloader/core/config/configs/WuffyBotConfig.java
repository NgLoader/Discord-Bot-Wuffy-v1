package de.ngloader.core.config.configs;

import java.util.List;

import de.ngloader.core.config.Config;
import de.ngloader.core.config.IConfig;
import net.dv8tion.jda.core.OnlineStatus;

@Config(path = "./wuffy/bot.json", sourcePath = "/config/bot.json")
public class WuffyBotConfig implements IConfig {

	public Boolean enabled;

	public String token;

	public String mentionId;

	public List<String> admins;

	public OnlineStatus status;
	public GameConfig game;

	public ShardConfig sharding;
}