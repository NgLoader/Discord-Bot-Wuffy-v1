package net.wuffy.notification.database;

import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.wuffy.common.logger.Logger;
import net.wuffy.core.database.mongo.MongoConfig;

public class MongoExtensionGuild {

	private final MongoConfig config;

	private MongoClient client;
	private MongoDatabase database;

	private final SingleResultCallback<BulkWriteResult> printBatchResult = new SingleResultCallback<BulkWriteResult>() {

		public void onResult(BulkWriteResult result, Throwable throwable) {
			Logger.debug("Database MongoDB", String.format("Inserted: %s, Deleted: %s, Modified: %s, Matched: %s",
					Integer.toString(result.getInsertedCount()),
					Integer.toString(result.getDeletedCount()),
					result.isModifiedCountAvailable() ? Integer.toString(result.getModifiedCount()) : "Not Avaivible",
					Integer.toString(result.getMatchedCount())));

			if(throwable != null) {
				Logger.fatal("Database MongoDB", "Failed to bulk write", throwable);
				throwable.printStackTrace();
			}
		};
	};

	public MongoExtensionGuild(MongoConfig config) {
		this.config = config;
	}

	public void connect() {
		this.client = MongoClients.create(new ConnectionString(String.format("mongodb://%s:%s@%s:%s/%s",
				this.config.username,
				this.config.password,
				this.config.address,
				Integer.toString(this.config.port),
				this.config.database)));

		this.database = this.client.getDatabase(config.database);
	}

	public void disconnect() {
		if(this.client != null)
			this.client.close();
		this.client = null;
		this.database = null;
	}

	public MongoCollection<Document> getCollection(String name) {
		return this.database.getCollection(this.config.collectionPrefix + name);
	}

	public <T> MongoCollection<T> getCollection(String name, Class<T> type) {
		return this.database.getCollection(this.config.collectionPrefix + name, type);
	}

	public SingleResultCallback<BulkWriteResult> getPrintBatchResult() {
		return printBatchResult;
	}
}