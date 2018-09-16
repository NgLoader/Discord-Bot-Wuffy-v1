package net.wuffy.notification;

import net.wuffy.common.config.Config;
import net.wuffy.common.config.IConfig;
import net.wuffy.core.database.DatabaseConfig;

@Config(path = "./wuffy/config.json", sourcePath = "/config/config.json")
public class AnnouncementConfig implements IConfig {

	public DatabaseConfig database;

	public TwitchConfig twitch;

	public YoutubeConfig youtube;

	class TwitchConfig {
		public Boolean enabled;

		public String token;
	}

	class YoutubeConfig {
		public Boolean enabled;

		public String token;
	}
}