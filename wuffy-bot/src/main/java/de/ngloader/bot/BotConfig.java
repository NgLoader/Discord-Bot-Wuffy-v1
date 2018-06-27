package de.ngloader.bot;

import java.util.List;

import de.ngloader.core.GameConfig;
import de.ngloader.core.config.Config;
import de.ngloader.core.config.IConfig;
import net.dv8tion.jda.core.OnlineStatus;

@Config(path = "./wuffy/bot.json", sourcePath = "/config/bot.json")
public class BotConfig implements IConfig {

	public Boolean enabled;

	public String token;

	public List<Long> admins;

	public String mentionId;

	public OnlineStatus status;
	public GameConfig game;

	public ShardConfig sharding;
}