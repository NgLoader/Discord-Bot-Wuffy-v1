package de.ngloader.database.mongo;

public class MongoConfig {

	public boolean enabled;
	public String address;
	public int port;
	public String username;
	public String password;
	public String database;
	public String collectionPrefix;
	public Ssl ssl;

	public class Ssl {
		public boolean enabled;
		public boolean invalidHostNames;
		public String trustStoreFile;
		public String trustStorePasswordFile;
		public String keyStoreFile;
		public String keyStorePassword;
	}
}