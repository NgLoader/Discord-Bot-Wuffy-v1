package net.wuffy.bot.database.mongo.impl;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;

import net.dv8tion.jda.core.entities.Guild;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.mongo.MongoStorageImpl;
import net.wuffy.core.Core;
import net.wuffy.core.database.mongo.MongoBulkWriteSystem;

public class MongoDBGuild extends DBGuild {

	private static final Gson GSON = new GsonBuilder().create();
	
	private static MongoCollection<Document> collection;
	private static MongoBulkWriteSystem bulkWriteSystem;

	private MongoStorageImpl storage;

	private MongoGuildCache guildCache;
	private Bson filter;

	public MongoDBGuild(MongoStorageImpl storage, Core core, Guild guild) {
		super(core, guild);

		this.storage = storage;

		if(MongoDBGuild.collection == null) {
			MongoDBGuild.collection = this.storage.getStorage().getCollection("guild");

			MongoDBGuild.bulkWriteSystem = this.storage.getBulkWriteSystemAdapter().get("guild");
			MongoDBGuild.bulkWriteSystem.enableBulkWrite(MongoDBGuild.collection);
		}

		this.filter = Filters.eq("_guildId", Long.toString(this.getIdLong()));
		Document found = MongoDBGuild.collection.find(this.filter).first();

		if(found == null) {
			this.guildCache = new MongoGuildCache(this.getIdLong()).construct();

			MongoDBGuild.bulkWriteSystem.queueBulkModel(new InsertOneModel<Document>(Document.parse(MongoDBGuild.GSON.toJson(this.guildCache))));
		} else {
			this.guildCache = MongoDBGuild.GSON.fromJson(found.toJson(), MongoGuildCache.class);
			this.filter = Filters.eq("_id", this.guildCache._id);
		}
	}

	public void queueBulk(Document update) {
		this.queueBulk(this.filter, update);
	}

	public void queueBulk(Bson filter, Document update) {
		MongoDBGuild.bulkWriteSystem.queueBulkModel(new UpdateOneModel<Document>(filter, update));
	}

	public void queueBulk(Document filter, Document update) {
		MongoDBGuild.bulkWriteSystem.queueBulkModel(new UpdateOneModel<Document>(filter, update));
	}

	public Document toDocument(Object object) {
		return Document.parse(MongoDBGuild.GSON.toJson(object));
	}

	@Override
	public void destroy() {
		this.storage = null;
		this.guildCache = null;
		this.filter = null;
	}

	@Override
	public boolean isMention() {
		return this.guildCache.command.mention;
	}

	@Override
	public void setMention(boolean mention) {
		this.guildCache.command.mention = mention;
		this.queueBulk(new Document("$set", new Document("command.mention", mention)));
		
	}

	@Override
	public List<String> getPrefixes() {
		return this.guildCache.command.prefixes;
	}

	@Override
	public void addPrefix(String prefix) {
		this.guildCache.command.prefixes.add(prefix);
		this.queueBulk(new Document("$addToSet", new Document("command.prefixes", prefix)));
	}

	@Override
	public void removePrefix(String prefix) {
		this.guildCache.command.prefixes.remove(prefix);
		this.queueBulk(new Document("$pull", new Document("command.prefixes", prefix)));
	}

	@Override
	public void setPrefix(String... prefix) {
		List<String> prefixes = Arrays.asList(prefix);

		this.guildCache.command.prefixes = prefixes;
		this.queueBulk(new Document("$set", new Document("command.prefixes", prefixes)));
	}

	@Override
	public boolean isDeleteExecuterMessage() {
		return this.guildCache.message.deleteExecuter;
	}

	@Override
	public void setDeleteExecuterMessage(boolean delete) {
		this.guildCache.message.deleteExecuter = delete;
		this.queueBulk(new Document("$set", new Document("message.deleteExecuter", delete)));
	}

	@Override
	public boolean isDeleteBotMessage() {
		return this.guildCache.message.deleteBot;
	}

	@Override
	public void setDeleteBotMessage(boolean delete) {
		this.guildCache.message.deleteBot = delete;
		this.queueBulk(new Document("$set", new Document("message.deleteBot", delete)));
	}

	@Override
	public List<EnumPermissionMode> getPermissionMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPermissionMode(EnumPermissionMode... permissionMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePermissionMode(EnumPermissionMode... permissionMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPermissionMode(List<EnumPermissionMode> permissionModeList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getPermissionChannel(EnumPermissionType type, Long channelId, String... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePermissionChannel(EnumPermissionType type, Long channelId, List<String> ids,
			String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getPermissionGlobal(EnumPermissionType type, String... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPermissionGlobal(EnumPermissionType type, String id, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPermissionGlobal(EnumPermissionType type, String id, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePermissionGlobal(EnumPermissionType type, String id, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean isPartner() {
		return this.guildCache.partner;
	}

	@Override
	public void setPartner(boolean partner) {
		this.guildCache.partner = partner;
		this.queueBulk(new Document("$set", new Document("partner", partner)));
	}

	@Override
	public void deleteFromDatabase() {
		MongoDBGuild.collection.findOneAndDelete(this.filter);
	}
}