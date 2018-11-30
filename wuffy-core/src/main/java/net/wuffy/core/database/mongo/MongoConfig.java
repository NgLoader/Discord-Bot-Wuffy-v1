package net.wuffy.core.database.mongo;

public class MongoConfig {

	public Boolean enabled;

	public String address;
	public Integer port;
	public String username;
	public String password;
	public String database;
	public String collectionPrefix;
	public Ssl ssl;

	public class Ssl {
		public Boolean enabled;
		public Boolean invalidHostNames;
		public String trustStoreFile;
		public String trustStorePasswordFile;
		public String keyStoreFile;
		public String keyStorePassword;
	}
}