package net.wuffy.core.database;

import java.util.Map;

import net.wuffy.common.config.IConfig;
import net.wuffy.core.database.locale.LocaleConfig;
import net.wuffy.core.database.mongo.MongoConfig;
import net.wuffy.core.database.poeditor.POEditorConfig;
import net.wuffy.core.database.sql.SQLConfig;

public class DatabaseConfig implements IConfig {

	public MongoConfig mongo;
	public SQLConfig sql;
	public LocaleConfig locale;
	public POEditorConfig poeditor;
	public Map<String, String> extensions;
}