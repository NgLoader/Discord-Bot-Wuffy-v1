package de.ngloader.api;

import java.util.List;

import de.ngloader.api.config.Config;
import de.ngloader.api.config.IConfig;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.OnlineStatus;

@Config(path = "./wuffy/credentials.json", sourcePath = "/config/credentials.json")
public class WuffyConfig implements IConfig {

	public AccountType accountType;

	public String token;

	public List<String> admins;

	public OnlineStatus status;
	public GameConfig game;

	public ShardConfig sharding;
}