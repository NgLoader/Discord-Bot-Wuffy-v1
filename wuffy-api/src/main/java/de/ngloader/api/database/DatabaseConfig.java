package de.ngloader.api.database;

import java.util.Map;

import de.ngloader.api.config.Config;
import de.ngloader.api.config.IConfig;
import de.ngloader.api.database.mongo.MongoConfig;
import de.ngloader.api.database.sql.SQLConfig;

@Config(path = "./wuffy/database.json", sourcePath = "/config/database.json")
public class DatabaseConfig implements IConfig {

	public MongoConfig mongo;
	public SQLConfig sql;
	public Map<String, String> extensions;
}