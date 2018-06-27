package de.ngloader.core.database;

import java.util.Map;

import de.ngloader.core.config.Config;
import de.ngloader.core.config.IConfig;
import de.ngloader.core.database.locale.LocaleConfig;
import de.ngloader.core.database.mongo.MongoConfig;
import de.ngloader.core.database.sql.SQLConfig;

@Config(path = "./wuffy/database.json", sourcePath = "/config/database.json")
public class DatabaseConfig implements IConfig {

	public MongoConfig mongo;
	public SQLConfig sql;
	public LocaleConfig locale;
	public Map<String, String> extensions;
}