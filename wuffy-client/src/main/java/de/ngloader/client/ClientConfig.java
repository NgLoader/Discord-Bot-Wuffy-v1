package de.ngloader.client;

import java.util.List;

import de.ngloader.core.GameConfig;
import de.ngloader.core.config.Config;
import de.ngloader.core.config.IConfig;
import net.dv8tion.jda.core.OnlineStatus;

@Config(path = "./wuffy/client.json", sourcePath = "/config/client.json")
public class ClientConfig implements IConfig {

	public Boolean enabled;

	public String token;

	public boolean mention;
	public String mentionId;

	public List<Long> admins;

	public OnlineStatus status;
	public GameConfig game;
}