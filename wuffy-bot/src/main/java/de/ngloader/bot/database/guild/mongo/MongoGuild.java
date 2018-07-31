package de.ngloader.bot.database.guild.mongo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.UpdateOneModel;

import de.ngloader.bot.database.BanInfo;
import de.ngloader.bot.database.BlockedInfo;
import de.ngloader.bot.database.MuteInfo;
import de.ngloader.bot.database.NotificationInfo;
import de.ngloader.bot.database.NotificationType;
import de.ngloader.bot.database.WarnInfo;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.mongo.MongoGuildCache.EnumEditOperator;
import de.ngloader.core.Core;
import de.ngloader.core.command.MessageType;
import net.dv8tion.jda.core.entities.Guild;

public class MongoGuild extends WuffyGuild {

	public static final Map<Object, Object> EMPTY_MAP = new HashMap<Object, Object>();
	public static final Map<String, NotificationInfo> EMPTY_MAP_NOTIFICATION = new HashMap<String, NotificationInfo>();

	private final Gson gson = new GsonBuilder()
	.registerTypeAdapter(ObjectId.class, new JsonDeserializer<ObjectId>() {

		@Override
		public ObjectId deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			return new ObjectId(json.getAsJsonObject().get("$oid").getAsString());
		}
	}).registerTypeAdapter(EnumPermissionType.class, new JsonSerializer<EnumPermissionType>() {

		@Override
		public JsonElement serialize(EnumPermissionType enumObject, Type type, JsonSerializationContext  context) throws JsonParseException {
			return new JsonPrimitive(enumObject.name());
		}
	}).registerTypeAdapter(EnumPermissionMode.class, new JsonSerializer<EnumPermissionMode>() {

		@Override
		public JsonElement serialize(EnumPermissionMode enumObject, Type type, JsonSerializationContext  context) throws JsonParseException {
			return new JsonPrimitive(enumObject.name());
		}
	}).registerTypeAdapter(EnumRoleRankingMode.class, new JsonSerializer<EnumRoleRankingMode>() {

		@Override
		public JsonElement serialize(EnumRoleRankingMode enumObject, Type type, JsonSerializationContext  context) throws JsonParseException {
			return new JsonPrimitive(enumObject.name());
		}
	}).registerTypeAdapter(NotificationType.class, new JsonSerializer<NotificationType>() {

		@Override
		public JsonElement serialize(NotificationType enumObject, Type type, JsonSerializationContext  context) throws JsonParseException {
			return new JsonPrimitive(enumObject.name());
		}
	}).registerTypeAdapter(NotificationType.class, new JsonDeserializer<NotificationType>() {

		@Override
		public NotificationType deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			for(NotificationType mode : NotificationType.values())
				if(mode.name().equals(json.getAsString()))
					return mode;

			return null;
		}
	}).registerTypeAdapter(EnumPermissionType.class, new JsonDeserializer<EnumPermissionType>() {

		@Override
		public EnumPermissionType deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			for(EnumPermissionType mode : EnumPermissionType.values())
				if(mode.name().equals(json.getAsString()))
					return mode;

			return null;
		}
	}).registerTypeAdapter(EnumPermissionMode.class, new JsonDeserializer<EnumPermissionMode>() {

		@Override
		public EnumPermissionMode deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			for(EnumPermissionMode mode : EnumPermissionMode.values())
				if(mode.name().equals(json.getAsString()))
					return mode;

			return null;
		}
	}).registerTypeAdapter(EnumRoleRankingMode.class, new JsonDeserializer<EnumRoleRankingMode>() {

		@Override
		public EnumRoleRankingMode deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			for(EnumRoleRankingMode mode : EnumRoleRankingMode.values())
				if(mode.name().equals(json.getAsString()))
					return mode;

			return null;
		}
	}).create();

	protected final MongoExtensionGuild extension;

	protected MongoGuildCache cache;

	protected Bson filter;

	public MongoGuild(Core core, Guild guild, MongoExtensionGuild extension) {
		super(core, guild);
		this.extension = extension;

		this.filter = Filters.eq("_guildId", Long.toString(this.getIdLong()));

		Document found = this.extension.getCollection().find(this.filter).first();

		if(found == null) {
			this.cache = new MongoGuildCache(this.getIdLong());
			this.extension.queueBulkModel(new InsertOneModel<Document>(Document.parse(gson.toJson(this.cache))));
		} else {
			this.cache = gson.fromJson(found.toJson(), MongoGuildCache.class);
			this.filter = Filters.eq("_id", this.cache._id);
		}
	}

	protected void queueBulk(Document update) {
		this.extension.queueBulkModel(new UpdateOneModel<Document>(this.filter, update));
	}

	protected void queueBulk(Document filter, Document update) {
		this.extension.queueBulkModel(new UpdateOneModel<Document>(filter, update));
	}

	protected Document toDocument(Object object) {
		return Document.parse(this.gson.toJson(object));
	}

	@Override
	public String getGuildLocale() {
		return this.cache.guildLocale;
	}

	@Override
	public void setGuildLocale(String locale) {
		this.cache.guildLocale = locale;
		this.queueBulk(new Document("$set", new Document("$guildLocale", locale)));
	}

	@Override
	public List<String> getPrefixes() {
		return this.cache.prefixes;
	}

	@Override
	public void addPrefix(String prefix) {
		this.cache.prefixes.add(prefix);
		this.queueBulk(new Document("$addToSet", new Document("prefixes", prefix)));
	}

	@Override
	public void removePrefix(String prefix) {
		this.cache.prefixes.remove(prefix);
		this.queueBulk(new Document("$pull", new Document("prefixes", prefix)));
	}

	@Override
	public void setPrefixes(List<String> prefixes) {
		this.cache.prefixes = prefixes;
		this.queueBulk(new Document("$set", new Document("prefixes", prefixes)));
	}

	@Override
	public Boolean isMention() {
		return this.cache.mention;
	}

	@Override
	public void setMention(boolean mention) {
		this.cache.mention = mention;
		this.queueBulk(new Document("$set", new Document("mention", mention)));
	}

	@Override
	public List<String> getDisabledCommands() {
		return this.cache.disabledCommands;
	}

	@Override
	public void addDisabledCommands(String command) {
		this.cache.disabledCommands.add(command);
		this.queueBulk(new Document("$pull", new Document("disabledCommands", command)));
	}

	@Override
	public void removeDisabledCommands(String command) {
		this.cache.disabledCommands.remove(command);
		this.queueBulk(new Document("$addToSet", new Document("disabledCommands", command)));
	}

	@Override
	public void setDisabledCommands(List<String> commands) {
		this.cache.disabledCommands = commands;
		this.queueBulk(new Document("$set", new Document("disabledCommands", commands)));
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
	public WarnInfo getWarn(Long userId) {
		return this.cache.userWarns.getOrDefault(Long.toString(userId), null);
	}

	@Override
	public void setWarn(Long userId, WarnInfo warnInfo) {
		this.cache.userWarns.put(Long.toString(userId), warnInfo);
		this.queueBulk(new Document("$set", new Document(String.format("user.warns.%s", Long.toString(userId)), this.toDocument(warnInfo))));
	}

	@Override
	public List<BlockedInfo> getBlockedHistory() {
		return this.cache.history.blocked;
	}

	@Override
	public void addBlockedHistory(BlockedInfo blockedInfo) {
		this.cache.history.blocked.add(blockedInfo);
		this.queueBulk(new Document("$addToSet", new Document("history.blocked", this.toDocument(blockedInfo))));
	}

	@Override
	public void removeBlockedHistory(BlockedInfo blockedInfo) {
		this.cache.history.blocked.remove(blockedInfo);
		this.queueBulk(new Document("$pull", new Document("history.blocked", this.toDocument(blockedInfo))));
	}

	@Override
	public void setBlockedHistory(List<BlockedInfo> blockedInfos) {
		this.cache.history.blocked = blockedInfos;
		this.queueBulk(new Document("$set", new Document("history.blocked", this.toDocument(blockedInfos))));
	}

	@Override
	public List<BanInfo> getBanHistory() {
		return this.cache.history.ban;
	}

	@Override
	public void addBanHistory(BanInfo banInfo) {
		this.cache.history.ban.add(banInfo);
		this.queueBulk(new Document("$addToSet", new Document("history.ban", this.toDocument(banInfo))));
	}

	@Override
	public void removeBanHistory(BanInfo banInfo) {
		this.cache.history.ban.remove(banInfo);
		this.queueBulk(new Document("$pull", new Document("history.ban", this.toDocument(banInfo))));
	}

	@Override
	public void setBanHistory(List<BanInfo> banInfos) {
		this.cache.history.ban = banInfos;
		this.queueBulk(new Document("$set", new Document("history.ban", this.toDocument(banInfos))));
	}

	@Override
	public List<MuteInfo> getMuteHistory() {
		return this.cache.history.mute;
	}

	@Override
	public void addMuteHistory(MuteInfo muteInfo) {
		this.cache.history.mute.add(muteInfo);
		this.queueBulk(new Document("$addToSet", new Document("history.mute", this.toDocument(muteInfo))));
	}

	@Override
	public void removeMuteHistory(MuteInfo muteInfo) {
		this.cache.history.mute.remove(muteInfo);
		this.queueBulk(new Document("$pull", new Document("history.mute", this.toDocument(muteInfo))));
	}

	@Override
	public void setMuteHistory(List<MuteInfo> muteInfos) {
		this.cache.history.mute = muteInfos;
		this.queueBulk(new Document("$set", new Document("history.mute", this.toDocument(muteInfos))));
	}

	@Override
	public List<WarnInfo> getWarnHistory() {
		return this.cache.history.warn;
	}

	@Override
	public void addWarnHistory(WarnInfo warnInfo) {
		this.cache.history.warn.add(warnInfo);
		this.queueBulk(new Document("$addToSet", new Document("history.warn", this.toDocument(warnInfo))));
	}

	@Override
	public void removeWarnHistory(WarnInfo warnInfo) {
		this.cache.history.warn.remove(warnInfo);
		this.queueBulk(new Document("$pull", new Document("history.warn", this.toDocument(warnInfo))));
	}

	@Override
	public void setWarnHistory(List<WarnInfo> warnInfos) {
		this.cache.history.warn = warnInfos;
		this.queueBulk(new Document("$set", new Document("history.warn", this.toDocument(warnInfos))));
	}

	@Override
	public Map<Long, Integer> getRoleRanking() {
		return this.cache.roleRanking.ranking.entrySet().stream().collect(Collectors.toMap(entry -> Long.parseLong(entry.getKey()), entry -> entry.getValue()));
	}

	@Override
	public void setRoleRanking(Long role, Integer ranking) {
		this.cache.roleRanking.ranking.put(Long.toString(role), ranking);
		this.queueBulk(new Document("$set", new Document(String.format("roleRanking.ranking.%s", Long.toString(role)), ranking)));
	}

	@Override
	public void setRoleRanking(Object... values) {
		Document update = new Document();

		for(int i = 0; (i + 1) < values.length; i++) {
			String role = Long.toString((Long) values[i]);
			Integer ranking = (Integer) values[++i];
			update.append(String.format("roleRanking.ranking.%s", role), ranking);
			this.cache.roleRanking.ranking.put(role, ranking);
		}

		if(!update.isEmpty())
			this.queueBulk(new Document("$set", update));
	}

	@Override
	public void setRoleRanking(Map<Long, Integer> ranking) {
		Map<String, Integer> convertedRanking = ranking.entrySet().stream().collect(Collectors.toMap(entry -> Long.toString(entry.getKey()), entry -> entry.getValue()));

		this.cache.roleRanking.ranking = convertedRanking;
		this.queueBulk(new Document("$set", new Document("roleRanking.ranking", convertedRanking)));
	}

	@Override
	public EnumRoleRankingMode getRoleRankingMode() {
		return this.cache.roleRanking.mode;
	}

	@Override
	public void setRoleRankingMode(EnumRoleRankingMode rankingMode) {
		this.cache.roleRanking.mode = rankingMode;
		this.queueBulk(new Document("$set", new Document("roleRanking.mode", rankingMode.name())));
	}

	@Override
	public List<EnumPermissionMode> getPermissionMode() {
		return this.cache.permission.mode;
	}

	@Override
	public void addPermissionMode(EnumPermissionMode... permissionMode) {
		List<EnumPermissionMode> permissionModeList = Arrays.asList(permissionMode);

		this.cache.permission.mode.addAll(permissionModeList);
		this.queueBulk(new Document("$addToSet", new Document("permission.mode", new Document("$each", permissionModeList.stream().map(mode -> mode.name()).collect(Collectors.toList())))));
	}

	@Override
	public void removePermissionMode(EnumPermissionMode... permissionMode) {
		List<EnumPermissionMode> permissionModeList = Arrays.asList(permissionMode);

		this.cache.permission.mode.removeAll(permissionModeList);
		this.queueBulk(new Document("$pullAll", new Document("permission.mode", permissionModeList.stream().map(mode -> mode.name()).collect(Collectors.toList()))));
	}

	@Override
	public void setPermissionMode(List<EnumPermissionMode> permissionModeList) {
		this.cache.permission.mode = permissionModeList;
		this.queueBulk(new Document("$set", new Document("permission.mode", permissionModeList)));
	}

	@Override
	public List<String> getPermissionChannel(EnumPermissionType type, Long channelId, String... ids) {
		List<String> permissions = new ArrayList<String>();
		List<String> idList = Arrays.asList(ids);
		String channel = Long.toString(channelId);

		if(this.cache.permission.channel.containsKey(type) &&
				this.cache.permission.channel.get(type).containsKey(channel))
			this.cache.permission.channel.get(type).get(channel).forEach((id, permission) -> {
				if(idList.contains(id))
					permissions.addAll(permission);
			});

		return permissions;
	}

	@Override
	public void setPermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions) {
		this.cache.permission.editChannel(EnumEditOperator.SET, type, Long.toString(channelId), id, permissions);
		this.queueBulk(new Document("$set", new Document(String.format("permission.channel.%s.%s.%s", type.name(), Long.toString(channelId), id), Arrays.asList(permissions))));
	}

	@Override
	public void setPermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions) {
		this.cache.permission.editChannel(EnumEditOperator.SET, type, Long.toString(channelId), ids, permissions);

		List<String> permissionList = Arrays.asList(permissions);
		Document update = new Document();

		ids.forEach(id -> update.append(String.format("permission.channel.%s.%s.%s", type.name(), Long.toString(channelId), id), permissionList));

		if(!update.isEmpty())
			this.queueBulk(new Document("$set", update));
	}

	@Override
	public void addPermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions) {
		this.cache.permission.editChannel(EnumEditOperator.ADD, type, Long.toString(channelId), id, permissions);
		this.queueBulk(new Document("$addToSet",
				new Document(String.format("permission.channel.%s.%s.%s", type.name(), Long.toString(channelId), id),
				new Document("$each", Arrays.asList(permissions)))));
	}

	@Override
	public void addPermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions) {
		this.cache.permission.editChannel(EnumEditOperator.ADD, type, Long.toString(channelId), ids, permissions);

		Document permissionList = new Document("$each", Arrays.asList(permissions));
		Document update = new Document();

		ids.forEach(id -> update.append(String.format("permission.channel.%s.%s.%s", type.name(), Long.toString(channelId), id), permissionList));

		if(!update.isEmpty())
			this.queueBulk(new Document("$addToSet", update));
	}

	@Override
	public void removePermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions) {
		this.cache.permission.editChannel(EnumEditOperator.REMOVE, type, Long.toString(channelId), id, permissions);
		this.queueBulk(new Document("$pullAll", new Document(String.format("permission.channel.%s.%s.%s", type.name(), Long.toString(channelId), id), Arrays.asList(permissions))));
	}

	@Override
	public void removePermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions) {
		this.cache.permission.editChannel(EnumEditOperator.REMOVE, type, Long.toString(channelId), ids, permissions);

		List<String> permissionList = Arrays.asList(permissions);
		Document update = new Document();

		ids.forEach(id -> update.append(String.format("permission.channel.%s.%s.%s", type.name(), Long.toString(channelId), id), permissionList));

		if(!update.isEmpty())
			this.queueBulk(new Document("$pullAll", update));
	}

	@Override
	public List<String> getPermissionGlobal(EnumPermissionType type, String... ids) {
		List<String> permissions = new ArrayList<String>();
		List<String> idList = Arrays.asList(ids);

		if(this.cache.permission.global.containsKey(type))
			this.cache.permission.global.get(type).forEach((id, permission) -> {
				if(idList.contains(id))
					permissions.addAll(permission);
			});

		return permissions;
	}

	@Override
	public void setPermissionGlobal(EnumPermissionType type, String id, String... permissions) {
		this.cache.permission.editGlobal(EnumEditOperator.SET, type, id, permissions);
		this.queueBulk(new Document("$set", new Document(String.format("permission.global.%s.%s", type.name(), id), Arrays.asList(permissions))));
	}

	@Override
	public void setPermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions) {
		this.cache.permission.editGlobal(EnumEditOperator.SET, type, ids, permissions);

		List<String> permissionList = Arrays.asList(permissions);
		Document update = new Document();

		ids.forEach(id -> update.append(String.format("permission.global.%s.%s", type.name(), id), permissionList));

		if(!update.isEmpty())
			this.queueBulk(new Document("$set", update));
	}

	@Override
	public void addPermissionGlobal(EnumPermissionType type, String id, String... permissions) {
		this.cache.permission.editGlobal(EnumEditOperator.ADD, type, id, permissions);
		this.queueBulk(new Document("$addToSet",
				new Document(String.format("permission.global.%s.%s", type.name(), id),
				new Document("$each", Arrays.asList(permissions)))));
	}

	@Override
	public void addPermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions) {
		this.cache.permission.editGlobal(EnumEditOperator.ADD, type, ids, permissions);

		Document permissionList = new Document("$each", Arrays.asList(permissions));
		Document update = new Document();

		ids.forEach(id -> update.append(String.format("permission.global.%s.%s", type.name(), id), permissionList));

		if(!update.isEmpty())
			this.queueBulk(new Document("$addToSet", update));
	}

	@Override
	public void removePermissionGlobal(EnumPermissionType type, String id, String... permissions) {
		this.cache.permission.editGlobal(EnumEditOperator.REMOVE, type, id, permissions);
		this.queueBulk(new Document("$pullAll", new Document(String.format("permission.global.%s.%s", type.name(), id), Arrays.asList(permissions))));
	}

	@Override
	public void removePermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions) {
		this.cache.permission.editGlobal(EnumEditOperator.REMOVE, type, ids, permissions);

		List<String> permissionList = Arrays.asList(permissions);
		Document update = new Document();

		ids.forEach(id -> update.append(String.format("permission.global.%s.%s", type.name(), id), permissionList));

		if(!update.isEmpty())
			this.queueBulk(new Document("$pullAll", update));
	}

	@Override
	public Boolean isAutoPrune() {
		return this.cache.autoprune.enabled;
	}

	@Override
	public void setAutoPrune(Boolean autoPrune) {
		this.cache.autoprune.enabled = autoPrune;

		this.queueBulk(new Document("$set", new Document("autoprune.enabled", autoPrune)));
	}

	@Override
	public Integer getAutoPruneDeleteDays() {
		return this.cache.autoprune.days;
	}

	@Override
	public void setAutoPruneDeleteDays(Integer days) {
		this.queueBulk(new Document("$set", new Document("autoprune.days", days)));
	}

	@Override
	public Boolean isMessageDeleteExecuter() {
		return this.cache.message.deleteExecuter;
	}

	@Override
	public void setMessageDeleteExecuter(Boolean delete) {
		this.cache.message.deleteExecuter = delete;

		this.queueBulk(new Document("$set", new Document("message.deleteExecuter", delete)));
	}

	@Override
	public Boolean isMessageDeleteBot() {
		return this.cache.message.deleteBot;
	}

	@Override
	public void setMessageDeleteBot(Boolean delete) {
		this.cache.message.deleteBot = delete;

		this.queueBulk(new Document("$set", new Document("message.deleteBot", delete)));
	}

	@Override
	public Boolean isMessageDeleteDelay(MessageType type) {
		return this.cache.message.deleteBot && this.cache.message.delays.containsKey(type);
	}

	@Override
	public Integer getMessageDeleteDelay(MessageType type) {
		return this.cache.message.delays.getOrDefault(type, 10);
	}

	@Override
	public Map<MessageType, Integer> getMessageDeleteDelays() {
		return this.cache.message.delays;
	}

	@Override
	public void setMessageDeleteDelay(MessageType type, Integer delay) {
		this.cache.message.delays.put(type, delay);

		this.queueBulk(new Document("$set", new Document(String.format("message.delays.%s", type.name()), delay)));
	}

	@Override
	public void removeMessageDeleteDelay(MessageType type) {
		this.cache.message.delays.remove(type);

		this.queueBulk(new Document("$unset", new Document(String.format("message.delays.%s", type.name()), "")));
	}

	@Override
	public String getMessageColorCode(MessageType type) {
		return this.cache.message.colors.get(type);
	}

	@Override
	public Map<MessageType, String> getMessageColorCodes() {
		return this.cache.message.colors;
	}

	@Override
	public void setMessageColorCode(MessageType type, String colorCode) {
		this.cache.message.colors.put(type, colorCode);

		this.queueBulk(new Document("$set", new Document(String.format("message.colors.%s", type.name()), colorCode)));
	}

	@Override
	public List<NotificationInfo> getNotifications(NotificationType type) {
		if(this.cache.notification.containsKey(type))
			return this.cache.notification.get(type);
		return new ArrayList<NotificationInfo>();
	}

	@Override
	public NotificationInfo getNotification(NotificationType type, String key) {
		for(NotificationInfo notificationInfo : this.getNotifications(type))
			if(notificationInfo.name.equalsIgnoreCase(key)) {
				return notificationInfo;
			}
		return null;
	}

	@Override
	public void addNotification(NotificationType type, NotificationInfo notificationInfo) {
		if(!this.cache.notification.containsKey(type))
			this.cache.notification.put(type, new ArrayList<NotificationInfo>());
		this.cache.notification.get(type).add(notificationInfo);

		this.queueBulk(new Document("$addToSet", new Document(String.format("notification.%s", type.name()), this.toDocument(notificationInfo))));
	}

	@Override
	public void updateNotification(NotificationType type, String key, NotificationInfo info) {
		this.removeNotification(type, key);
		this.addNotification(type, info);
	}

	@Override
	public void removeNotification(NotificationType type, String key) {
		NotificationInfo notification = getNotification(type, key);

		if(this.cache.notification.containsKey(type))
			this.cache.notification.get(type).remove(notification);

		this.queueBulk(new Document("$pull", new Document(String.format("notification.%s", type.name()), this.toDocument(notification))));
	}

	@Override
	public void setNotificationMessage(NotificationType type, String key, String message) {
		NotificationInfo info = this.getNotification(type, key);

		if(info != null) {
			if(this.cache.notification.containsKey(type))
				this.cache.notification.get(type).remove(info);
			else
				this.cache.notification.put(type, new ArrayList<NotificationInfo>());

			info.message = message;

			this.cache.notification.get(type).add(info);
		}

		this.queueBulk(
				new Document("_guildId", Long.toString(this.getIdLong())).append(String.format("notification.%s.name", type.name()), key),
				new Document("$set", new Document(String.format("notification.%s.$.message", type.name()), message)));
	}

	@Override
	public void setNotification(NotificationType type, List<NotificationInfo> notificationInfos) {
		this.cache.notification.put(type, notificationInfos);

		this.queueBulk(new Document("$set", new Document("notification", notificationInfos.stream().collect(Collectors.toMap(key -> key, value -> this.toDocument(value))))));
	}
}