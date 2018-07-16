package de.ngloader.bot.database.user.mongo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;

import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.mongo.MongoBulkWriteSystem;
import de.ngloader.core.database.mongo.MongoStorage;
import net.dv8tion.jda.core.entities.User;

public class MongoExtensionUser extends MongoBulkWriteSystem implements IExtensionUser<WuffyUser> {

	private final Map<Long, WuffyUser> CACHED_USER = new HashMap<Long, WuffyUser>();

	@Override
	public void registered(MongoStorage storage) {
		this.enableBulkWrite(storage.getCollection("user"), "User");

		AtomicBoolean indexExist = new AtomicBoolean(false);
		this.bulkCollection.listIndexes().forEach(new Block<Document>() {

			@Override
			public void apply(Document t) {
				if(t.get("name", "").equals("_userId_"))
					indexExist.set(true);
			}
		});

		if(!indexExist.get())
			this.bulkCollection.createIndex(new Document("_userId", -1L), new IndexOptions().name("_userId_").unique(true).background(false).sparse(true));
	}

	@Override
	protected void unregistered() {
		this.disableBulkWrite();

		this.CACHED_USER.clear();
	}

	@Override
	public WuffyUser getUser(User user) {
		Long longId = user.getIdLong();

		if(!CACHED_USER.containsKey(longId))
			CACHED_USER.put(longId, new MongoUser(this.core, user, this));

		return CACHED_USER.get(longId);
	}

	public MongoCollection<Document> getCollection() {
		return this.bulkCollection;
	}
}