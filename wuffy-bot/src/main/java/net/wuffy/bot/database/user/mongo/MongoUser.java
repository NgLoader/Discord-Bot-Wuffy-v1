package net.wuffy.bot.database.user.mongo;

import java.lang.reflect.Type;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.database.BlockedInfo;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.core.Core;

public class MongoUser extends WuffyUser {

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(ObjectId.class, new JsonDeserializer<ObjectId>() {

				@Override
				public ObjectId deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
					return new ObjectId(json.getAsJsonObject().get("$oid").getAsString());
				}
			}).create();

	protected final MongoExtensionUser extension;

	protected MongoUserCache cache;

	protected Bson filter;

	public MongoUser(Core core, User user, MongoExtensionUser extension) {
		super(core, user);
		this.extension = extension;

		this.filter = Filters.eq("_userId", Long.toString(this.getIdLong()));

		Document found = this.extension.getCollection().find(this.filter).first();

		if(found == null) {
			this.cache = new MongoUserCache(this.getIdLong());
			this.extension.queueBulkModel(new InsertOneModel<Document>(Document.parse(gson.toJson(this.cache))));
		} else {
			this.cache = gson.fromJson(found.toJson(), MongoUserCache.class);
			this.filter = Filters.eq("_id", this.cache._id);
		}
	}

	protected void queueBulk(Document update) {
		this.extension.queueBulkModel(new UpdateOneModel<Document>(this.filter, update));
	}

	protected Document toDocument(Object object) {
		return Document.parse(this.gson.toJson(object));
	}

	@Override
	public BlockedInfo getBlocked() {
		return this.cache.blocked;
	}

	@Override
	public void setBlocked(BlockedInfo blockedInfo) {
		this.cache.blocked = blockedInfo;
		this.queueBulk(new Document("$set", new Document("blocked", this.toDocument(blockedInfo))));
	}

	@Override
	public String getUserLocale() {
		return this.cache.userLocale;
	}

	@Override
	public String getUserLocale(String defaultValue) {
		return this.getUserLocale() != null ? this.getUserLocale() : defaultValue;
	}

	@Override
	public void setUserLocale(String locale) {
		this.cache.userLocale = locale;
		this.queueBulk(new Document("$set", new Document("userLocale", locale)));
	}

	@Override
	public boolean isAlphaTester() {
		return this.cache.alphaTester != null && this.cache.alphaTester;
	}

	@Override
	public void setAlphaTester(boolean alphaTester) {
		this.cache.alphaTester = alphaTester;

		this.queueBulk(new Document("$set", new Document("alphaTester", alphaTester)));
	}
}