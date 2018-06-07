package de.ngloader.database.mongo;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.ngloader.database.api.Storage;

public final class MongoStorage extends Storage<MongoStorage> {

	private final MongoConfig config;

	private MongoClient client;
	private MongoDatabase database;

	public MongoStorage(MongoConfig config) {
		this.config = config;
	}

	@Override
	protected void connect() {
		MongoCredential credential;

		if(config.ssl.enabled) {
			credential = MongoCredential.createMongoX509Credential(this.config.username);
			System.setProperty("javax.net.ssl.trustStore", this.config.ssl.trustStoreFile);
			System.setProperty("javax.net.ssl.trustStorePassword", this.config.ssl.trustStorePasswordFile);
			System.setProperty("javax.net.ssl.keyStore", this.config.ssl.keyStoreFile);
			System.setProperty("javax.net.ssl.keyStorePassword", this.config.ssl.keyStorePassword);
		} else
			credential = MongoCredential.createCredential(this.config.username, this.config.database, this.config.password.toCharArray());

		ServerAddress address = new ServerAddress(this.config.address, this.config.port);
		MongoClientOptions options = MongoClientOptions.builder().sslEnabled(this.config.ssl.enabled).sslInvalidHostNameAllowed(this.config.ssl.invalidHostNames).build();
		this.client = new MongoClient(address, credential, options);
		this.database = this.client.getDatabase(config.database);
	}

	@Override
	protected void disconnect() {
		if(this.client != null)
			this.client.close();
		this.client = null;
		this.database = null;
	}

	@Override
	public boolean isOpen() {
		return this.client != null; //TODO find a better check
	}

	public MongoCollection<Document> getCollection(String name) {
		return this.database.getCollection(this.config.collectionPrefix + name);
	}

	public <T> MongoCollection<T> getCollection(String name, Class<T> type) {
		return this.database.getCollection(this.config.collectionPrefix + name, type);
	}
}
