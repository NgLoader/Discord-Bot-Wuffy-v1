package de.ngloader.bootstrap;

import java.util.List;

import de.ngloader.bot.BotConfig;
import de.ngloader.client.ClientConfig;
import de.ngloader.core.config.Config;
import de.ngloader.core.config.IConfig;

@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class GlobalConfig implements IConfig {

	public List<Long> admins;

	public List<BotConfig> bots;

	public List<ClientConfig> clients;
}