package net.wuffy.bot.database.mongo.impl;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.database.DBUser;
import net.wuffy.bot.database.mongo.MongoStorageImpl;
import net.wuffy.core.Core;

public class MongoDBUser extends DBUser {

	private static MongoCollection<Document> collection;

	private static Document EMPTY_DOCUMENT = new Document();

	private MongoStorageImpl storage;

	private Bson filter;

	public MongoDBUser(MongoStorageImpl storage, Core core, User user) {
		super(core, user);

		this.storage = storage;

		if(MongoDBUser.collection == null)
			MongoDBUser.collection = this.storage.getStorage().getCollection("user");

		this.filter = Filters.eq("_userId", Long.toString(this.getIdLong()));
		Document found = MongoDBUser.collection.find(this.filter).first();

		if(found != null)
			this.filter = Filters.eq("_id", found.getObjectId("_id"));
		else
			MongoDBUser.collection.insertOne(new Document("_userId", Long.toString(this.getIdLong())));
	}

	@Override
	public void destroy() {
		this.storage = null;
		this.filter = null;
	}

	@Override
	public boolean isAlphaTester() {
		return this.getDocument().getBoolean("alphaTester", false);
	}

	@Override
	public void setAlphaTester(boolean tester) {
		MongoDBUser.collection.updateOne(this.filter, new Document("$set", new Document("alphaTester", tester)));
	}

	public Document getDocument() {
		Document found = MongoDBUser.collection.find(this.filter).first();

		if(found != null)
			return found;
		return MongoDBUser.EMPTY_DOCUMENT;
	}
}