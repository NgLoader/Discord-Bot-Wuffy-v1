package net.wuffy.core;

import java.util.List;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.wuffy.core.config.IConfig;
import net.wuffy.core.database.DatabaseConfig;

public class CoreConfig implements IConfig {

	public Boolean enabled;

	public String instanceName;
	public String instanceVersion;

	public String token;

	public List<Long> admins;

	public DatabaseConfig database;

	public OnlineStatus status;
	public GameConfig game;

	public class GameConfig {
		public GameType gameType;
		public String name;
		public String url;
	}
}