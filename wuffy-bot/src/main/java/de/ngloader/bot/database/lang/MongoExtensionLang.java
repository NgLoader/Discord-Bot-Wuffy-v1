package de.ngloader.bot.database.lang;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.mongo.MongoStorage;

public class MongoExtensionLang extends StorageProvider<MongoStorage> implements IExtensionLang<WuffyLang> {

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(ObjectId.class, new JsonSerializer<ObjectId>() {
				@Override
				public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("$oid", src.toString());
					return jsonObject;
				}
			}).registerTypeAdapter(ObjectId.class, new JsonDeserializer<ObjectId>() {
			
				@Override
				public ObjectId deserialize(JsonElement json, Type type, JsonDeserializationContext context)
						throws JsonParseException {
					return new ObjectId(json.getAsJsonObject().get("$oid").getAsString());
				}
	}).create();

	private MongoCollection<Document> collection;

	@Override
	public void registered(MongoStorage storage) {
		this.collection = storage.getCollection("lang");
	}

	@Override
	public WuffyLang getLang(String lang) {
		Document document = this.collection.find(Filters.eq("lang", lang)).first();
		gson.fromJson(document.toJson(), LangDocument.class);
		return null;
	}

	@Override
	public List<WuffyLang> getLangs() {
		return new ArrayList<>();
	}

	private class LangDocument {

		public String lang;
		public Map<String, String> translations;
	}
}