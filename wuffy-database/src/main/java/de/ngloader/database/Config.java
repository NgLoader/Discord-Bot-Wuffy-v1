package de.ngloader.database;

import java.util.Map;

import de.ngloader.api.database.mongo.MongoConfig;
import de.ngloader.api.database.sql.SQLConfig;

class Config {

	MongoConfig mongo;
	SQLConfig sql;
	Map<String, String> extensions;
}