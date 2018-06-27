package de.ngloader.core;

import java.util.List;

import de.ngloader.core.config.IConfig;
import de.ngloader.core.database.DatabaseConfig;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game.GameType;

public class CoreConfig implements IConfig {

	public Boolean enabled;

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