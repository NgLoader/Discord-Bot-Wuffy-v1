package de.ngloader.api;

import java.util.List;

import de.ngloader.api.config.Config;
import de.ngloader.api.config.IConfig;
import net.dv8tion.jda.core.OnlineStatus;

@Config(path = "./wuffy/client.json", sourcePath = "/config/client.json")
public class WuffyClientConfig implements IConfig {

	public Boolean enabled;

	public String token;

	public boolean mention;
	public String mentionId;

	public List<String> admins;

	public OnlineStatus status;
	public GameConfig game;
}