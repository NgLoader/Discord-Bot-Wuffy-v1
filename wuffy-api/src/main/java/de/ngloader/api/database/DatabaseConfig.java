package de.ngloader.api.database;

import java.util.Map;

import de.ngloader.api.database.mongo.MongoConfig;
import de.ngloader.api.database.sql.SQLConfig;

class DatabaseConfig {

	MongoConfig mongo;
	SQLConfig sql;
	Map<String, String> extensions;
}