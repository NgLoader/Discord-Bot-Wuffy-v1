package de.ngloader.database.impl.guild;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import de.ngloader.api.database.IStorageProvider;
import de.ngloader.api.database.impl.guild.GuildConfig;
import de.ngloader.api.database.impl.guild.GuildMusicPlaylist;
import de.ngloader.api.database.impl.guild.GuildSettings;
import de.ngloader.api.database.impl.guild.IExtensionGuild;
import de.ngloader.api.database.mongo.MongoStorage;

public class MongoExtensionGuild implements IStorageProvider<MongoStorage>, IExtensionGuild {

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(ObjectId.class, new JsonDeserializer<ObjectId>() {

				@Override
				public ObjectId deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
					return new ObjectId(json.getAsJsonObject().get("$oid").getAsString());
				}
			})
//			.setPrettyPrinting()
			.create();

	private final List<Long> cachedGuildIds = new CopyOnWriteArrayList<>();

	private MongoCollection<Document> collection;

	@Override
	public void registered(MongoStorage storage) {
		this.collection = storage.getCollection("guild");
	}

	@Override
	public GuildSettings getGuildSettings(long longId) {
		if(!cachedGuildIds.contains(longId)) {
			if(collection.find(Filters.eq("guildId", longId)).first() == null)
				collection.insertOne(Document.parse(gson.toJson(new GuildConfig(longId))));
			else

			cachedGuildIds.add(longId);
		}

		return gson.fromJson(collection.find(Filters.eq("guildId", longId)).first().get("settings", Document.class).toJson(), GuildSettings.class);
	}

	@Override
	public void setGuildSettings(long longId, GuildSettings guildConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPlaylist(long longId, GuildMusicPlaylist playlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GuildMusicPlaylist> getPlaylists(long longId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePlaylist(long longId, GuildMusicPlaylist playlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePlaylist(long longId, GuildMusicPlaylist playlist) {
		// TODO Auto-generated method stub
		
	}
}