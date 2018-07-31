package de.ngloader.notification;

import de.ngloader.core.config.Config;
import de.ngloader.core.config.IConfig;
import de.ngloader.core.database.DatabaseConfig;

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