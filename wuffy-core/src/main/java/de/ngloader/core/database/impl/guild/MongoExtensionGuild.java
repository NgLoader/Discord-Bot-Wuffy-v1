package de.ngloader.core.database.impl.guild;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.mongo.MongoStorage;

public class MongoExtensionGuild implements IStorageProvider<MongoStorage>, IExtensionGuild {

	//TODO fill out
//	private final Gson gson = new GsonBuilder()
//			.registerTypeAdapter(ObjectId.class, new JsonDeserializer<ObjectId>() {
//
//				@Override
//				public ObjectId deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
//					return new ObjectId(json.getAsJsonObject().get("$oid").getAsString());
//				}
//			})
//			.create();
//
//	private MongoCollection<Document> collection;

	@Override
	public void registered(MongoStorage storage) {
//		this.collection = storage.getCollection("guild");
	}
}