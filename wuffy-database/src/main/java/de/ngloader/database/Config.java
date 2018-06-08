package de.ngloader.database;

import java.util.Map;

import de.ngloader.database.mongo.MongoConfig;
import de.ngloader.database.sql.SQLConfig;

class Config {

	MongoConfig mongo;
	SQLConfig sql;
	Map<String, String> extensions;
}