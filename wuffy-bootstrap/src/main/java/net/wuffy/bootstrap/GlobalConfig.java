package net.wuffy.bootstrap;

import java.util.List;

import net.wuffy.bot.BotConfig;
import net.wuffy.client.ClientConfig;
import net.wuffy.core.config.Config;
import net.wuffy.core.config.IConfig;

@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class GlobalConfig implements IConfig {

	public List<Long> admins;

	public List<BotConfig> bots;

	public List<ClientConfig> clients;
}