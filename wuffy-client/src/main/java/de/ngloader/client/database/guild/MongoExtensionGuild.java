package de.ngloader.client.database.guild;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.mongo.MongoStorage;

public class MongoExtensionGuild extends StorageProvider<MongoStorage> implements IExtensionGuild<WuffyGuild, WuffyMember> {

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

	@Override
	public WuffyGuild getGuild(Long guildId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WuffyMember getMemeber(Long guildId, Long memberId) {
		// TODO Auto-generated method stub
		return null;
	}
}