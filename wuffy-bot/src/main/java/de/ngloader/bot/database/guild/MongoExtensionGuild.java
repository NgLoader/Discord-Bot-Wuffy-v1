package de.ngloader.bot.database.guild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.mongo.MongoStorage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

public class MongoExtensionGuild extends StorageProvider<MongoStorage> implements IExtensionGuild<WuffyGuild, WuffyMember>, IWuffyExtensionGuild {

//import java.lang.reflect.Type;
//
//import org.bson.types.ObjectId;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParseException;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;
//
//	private final Gson gson = new GsonBuilder()
//			.registerTypeAdapter(ObjectId.class, new JsonSerializer<ObjectId>() {
//
//				@Override
//				public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context) {
//					JsonObject jsonObject = new JsonObject();
//					jsonObject.addProperty("$oid", src.toString());
//					return jsonObject;
//				}
//			}).registerTypeAdapter(ObjectId.class, new JsonDeserializer<ObjectId>() {
//
//				@Override
//				public ObjectId deserialize(JsonElement json, Type type, JsonDeserializationContext context)
//						throws JsonParseException {
//					return new ObjectId(json.getAsJsonObject().get("$oid").getAsString());
//				}
//	}).create();

	private static final Document EMPTY_DOCUMENT = new Document();

	private MongoCollection<Document> collection;

	@Override
	public void registered(MongoStorage storage) {
		this.collection = storage.getCollection("guild");
	}

	private Document getDocument(Guild guild) {
		Document document = this.collection.find(Filters.eq("guildId", guild.getIdLong())).first();

		if(document == null) {
			Document insertDocument = new Document("guildId", guild.getIdLong())
					.append("prefixes", Arrays.asList("~"));

			this.collection.insertOne(insertDocument);

			return insertDocument;
		}

		return document;
	}

	private void execute(Guild guild, Document update) {
		Bson filter = Filters.eq("guildId", guild.getIdLong());

		if(this.collection.find(filter).first() == null)
			this.collection.insertOne(new Document("guildId", guild.getIdLong()));
		this.collection.updateOne(filter, update);
	}

	@Override
	public WuffyGuild getGuild(Guild guild) {
		return new WuffyGuild(this.core, guild, this);
	}

	@Override
	public WuffyMember getMemeber(Guild guild, Member member) {
		return new WuffyMember(this.core, member, this);
	}

	@Override
	public String getLocale(Guild guild) {
		return this.getDocument(guild).get("locale", "en-US");
	}

	@Override
	public void setLocale(Guild guild, String locale) {
		this.execute(guild, new Document("$set", new Document("locale", locale)));
	}

	@Override
	public List<String> getPrefixes(Guild guild) {
		return this.getDocument(guild).get("prefixes", Arrays.asList( "~" ));
	}

	@Override
	public void addPrefix(Guild guild, String prefix) {
		this.execute(guild, new Document("$push", new Document("prefixes", prefix)));
	}

	@Override
	public void removePrefix(Guild guild, String prefix) {
		this.execute(guild, new Document("$pull", new Document("prefixes", prefix)));
	}

	@Override
	public void setPrefixes(Guild guild, List<String> prefixes) {
		this.execute(guild, new Document("$set", new Document("prefixes", prefixes)));
	}

	@Override
	public boolean isMention(Guild guild) {
		return this.getDocument(guild).get("mention", true);
	}

	@Override
	public void setMention(Guild guild, boolean mention) {
		this.execute(guild, new Document("$set", new Document("mention", mention)));
	}

	@Override
	public String getInvite(Guild guild) {
		return this.getDocument(guild).get("invite", "");
	}

	@Override
	public void setInvite(Guild guild, String invite) {
		this.execute(guild, new Document("$set", new Document("invite", invite)));
	}

	@Override
	public List<String> getPermission(Guild guild, Long channel, User user) {
		return this.getDocument(guild).get(String.format("perm.%s.users.%s", Long.toString(channel), Long.toString(user.getIdLong())), new ArrayList<String>());
	}

	@Override
	public void setPermission(Guild guild, Long channel, User user, List<String> permission) {
		this.execute(guild, new Document("$set", new Document(String.format("perm.%s.users.%s", Long.toString(channel), Long.toString(user.getIdLong())), permission)));
	}

	@Override
	public void addPermission(Guild guild, Long channel, User user, List<String> permission) {
		this.execute(guild, new Document("$addToSet", new Document(String.format("perm.%s.users.%s", Long.toString(channel), Long.toString(user.getIdLong())), new Document("$each", permission))));
	}

	@Override
	public void removePermission(Guild guild, Long channel, User user, List<String> permission) {
		this.execute(guild, new Document("$pullAll", new Document(String.format("perm.%s.users.%s", Long.toString(channel), Long.toString(user.getIdLong())), permission)));
	}

	@Override
	public boolean hasPermission(Guild guild, Long channel, User user, List<String> permission) {
		return this.getDocument(guild)
				.get("perm", EMPTY_DOCUMENT)
				.get(Long.toString(channel), EMPTY_DOCUMENT)
				.get("users", EMPTY_DOCUMENT)
				.get(Long.toString(user.getIdLong()), new ArrayList<String>())
					.stream()
					.anyMatch(perm -> permission.contains(perm));
	}

	@Override
	public List<String> getPermission(Guild guild, Long channel, List<Role> group) {
		Document document = this.getDocument(guild);
		List<String> permission = new ArrayList<String>();

		group.forEach(g -> permission.addAll(document.get(String.format("perm.%s.groups.%s", Long.toString(channel), Long.toString(g.getIdLong())), new ArrayList<String>())));

		return permission;
	}

	@Override
	public void setPermission(Guild guild, Long channel, List<Role> group, List<String> permission) {
		group.forEach(g -> this.execute(guild, new Document("$set", new Document(String.format("perm.%s.groups.%s", Long.toString(channel), Long.toString(g.getIdLong())), permission)))); //TODO use bulk write
	}

	@Override
	public void addPermission(Guild guild, Long channel, List<Role> group, List<String> permission) {
		group.forEach(g -> this.execute(guild, new Document("$addToSet", new Document(String.format("perm.%s.groups.%s", Long.toString(channel), Long.toString(g.getIdLong())), new Document("$each", permission))))); //TODO use bulk write
	}

	@Override
	public void removePermission(Guild guild, Long channel, List<Role> group, List<String> permission) {
		group.forEach(g -> this.execute(guild, new Document("$pullAll", new Document(String.format("perm.%s.groups.%s", Long.toString(channel), Long.toString(g.getIdLong())), permission)))); //TODO use bulk write
	}

	@Override
	public boolean hasPermission(Guild guild, Long channel, List<Role> group, List<String> permission) {
		Document document = this.getDocument(guild);
		List<String> permissions = new ArrayList<String>();

		group.forEach(g -> permissions.addAll(document.get("perm", EMPTY_DOCUMENT)
				.get(Long.toString(channel), EMPTY_DOCUMENT)
				.get("users", EMPTY_DOCUMENT)
				.get(Long.toString(g.getIdLong()), new ArrayList<String>())));

		return permission.stream().anyMatch(perm -> permissions.contains(perm));
	}
}