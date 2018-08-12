package de.ngloader.bot.database.guild.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.BanInfo;
import de.ngloader.bot.database.BlockedInfo;
import de.ngloader.bot.database.MuteInfo;
import de.ngloader.bot.database.NotificationInfo;
import de.ngloader.bot.database.NotificationType;
import de.ngloader.bot.database.WarnInfo;
import de.ngloader.bot.database.guild.WuffyGuild.EnumPermissionMode;
import de.ngloader.bot.database.guild.WuffyGuild.EnumPermissionType;
import de.ngloader.bot.database.guild.WuffyGuild.EnumRoleRankingMode;

public class MongoGuildCache {

	private static final List<String> DEFAULT_PREFIX_LIST = Arrays.asList("~");
	private static final List<EnumPermissionMode> DEFAULT_PERMISSION_MODE_LIST = Arrays.asList(EnumPermissionMode.GLOBAL_USER, EnumPermissionMode.GLOBAL_ROLE);
	private static final EnumRoleRankingMode DEFAULT_RANKING_MODE = EnumRoleRankingMode.DISCORD;
	private static final Map<MessageType, Integer> DEFAULT_AUTO_PRUNE_DELAYS = new HashMap<MessageType, Integer>();

	static {
		MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS.put(MessageType.SUCCESS, 4);
		MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS.put(MessageType.INFO, 60);
		MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS.put(MessageType.PICTURE, 60);
		MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS.put(MessageType.LIST, 180);
		MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS.put(MessageType.HELP, 120);
		MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS.put(MessageType.PERMISSION, 12);
		MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS.put(MessageType.SYNTAX, 12);
		MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS.put(MessageType.ERROR, 8);
	}

	public MongoGuildCache(Long guildId) {
		this._guildId = Long.toString(guildId);
	}

	public ObjectId _id;

	public String _guildId;

	public BlockedInfo blocked;

	public String guildLocale = "en-US";

	public List<String> prefixes = MongoGuildCache.DEFAULT_PREFIX_LIST;
	public Boolean mention = true;

	public List<String> disabledCommands = new ArrayList<String>();

	public List<String> autoRole = new ArrayList<String>();

	public MongoGuildMessageCache message = new MongoGuildMessageCache();
	public MongoGuildHistoryCache history = new MongoGuildHistoryCache();
	public MongoGuildRankingCache roleRanking = new MongoGuildRankingCache();
	public MongoGuildAutoPruneCache autoprune = new MongoGuildAutoPruneCache();
	public MongoGuildPermissionCache permission = new MongoGuildPermissionCache();

	public Map<String, WarnInfo> userWarns = new HashMap<String, WarnInfo>();
	public Map<NotificationType, List<NotificationInfo>> notification = new HashMap<NotificationType, List<NotificationInfo>>();

	class MongoGuildMessageCache {
		public Boolean deleteExecuter = true;
		public Boolean deleteBot = true;

		public Map<MessageType, Integer> delays = new HashMap<MessageType, Integer>(MongoGuildCache.DEFAULT_AUTO_PRUNE_DELAYS);
		public Map<MessageType, String> colors = new HashMap<MessageType, String>();
	}

	class MongoGuildAutoPruneCache {
		public Boolean enabled = false;
		public Integer days = 0;
	}

	class MongoGuildHistoryCache {
		public List<BlockedInfo> blocked = new ArrayList<BlockedInfo>();
		public List<BanInfo> ban = new ArrayList<BanInfo>();
		public List<MuteInfo> mute = new ArrayList<MuteInfo>();
		public List<WarnInfo> warn = new ArrayList<WarnInfo>();
	}

	class MongoGuildRankingCache {
		public EnumRoleRankingMode mode = MongoGuildCache.DEFAULT_RANKING_MODE;
		public Map<String, Integer> ranking = new HashMap<String, Integer>();
	}

	class MongoGuildPermissionCache {
		public List<EnumPermissionMode> mode = MongoGuildCache.DEFAULT_PERMISSION_MODE_LIST;

		public Map<EnumPermissionType, Map<String, Map<String, List<String>>>> channel = new HashMap<EnumPermissionType, Map<String, Map<String, List<String>>>>();
		public Map<EnumPermissionType, Map<String, List<String>>> global = new HashMap<EnumPermissionType, Map<String, List<String>>>();

		public void editGlobal(EnumEditOperator operator, EnumPermissionType type, String id, List<String> permissions) {
			this.editGlobal(operator, type, Arrays.asList(id), permissions.toArray(new String[permissions.size()]));
		}

		public void editGlobal(EnumEditOperator operator, EnumPermissionType type, String id, String... permissions) {
			this.editGlobal(operator, type, Arrays.asList(id), permissions);
		}

		public void editGlobal(EnumEditOperator operator, EnumPermissionType type, List<String> ids, String... permissions) {
			switch (operator) {
			case ADD:
				if (!this.global.containsKey(type))
					this.global.put(type, new HashMap<String, List<String>>());

				for (String id : ids) {
					if (!this.global.get(type).containsKey(id))
						this.global.get(type).put(id, new ArrayList<String>());

					this.global.get(type).get(id).addAll(Arrays.asList(permissions));
				}
				break;

			case REMOVE:
				if (!this.global.containsKey(type))
					break;

				for (String id : ids) {
					if (!this.global.get(type).containsKey(id))
						continue;

					this.global.get(type).get(id).removeAll(Arrays.asList(permissions));
				}
				break;

			case SET:
				if (!this.global.containsKey(type))
					this.global.put(type, new HashMap<String, List<String>>());

				ids.forEach(id -> this.global.get(type).put(id, new ArrayList<String>(Arrays.asList(permissions))));
				break;

			default:
				break;
			}
		}

		public void editChannel(EnumEditOperator operator, EnumPermissionType type, String channelId, String id, List<String> permissions) {
			this.editChannel(operator, type, channelId, Arrays.asList(id), permissions.toArray(new String[permissions.size()]));
		}

		public void editChannel(EnumEditOperator operator, EnumPermissionType type, String channelId, String id, String... permissions) {
			this.editChannel(operator, type, channelId, Arrays.asList(id), permissions);
		}

		public void editChannel(EnumEditOperator operator, EnumPermissionType type, String channelId, List<String> ids, String... permissions) {
			switch (operator) {
			case ADD:
				if (!this.channel.containsKey(type))
					this.channel.put(type, new HashMap<String, Map<String, List<String>>>());
				if(!this.channel.get(type).containsKey(channelId))
					this.channel.get(type).put(channelId, new HashMap<String, List<String>>());

				for (String id : ids) {
					if (!this.channel.get(type).get(channelId).containsKey(id))
						this.channel.get(type).get(channelId).put(id, new ArrayList<String>());

					this.channel.get(type).get(channelId).get(id).addAll(Arrays.asList(permissions));
				}
				break;

			case REMOVE:
				if (!this.channel.containsKey(type) || !this.channel.get(type).containsKey(channelId))
					break;

				for (String id : ids) {
					if (!this.channel.get(type).get(channelId).containsKey(id))
						continue;

					this.channel.get(type).get(channelId).get(id).removeAll(Arrays.asList(permissions));
				}
				break;

			case SET:
				if (!this.channel.containsKey(type))
					this.channel.put(type, new HashMap<String, Map<String, List<String>>>());
				if(!this.channel.get(type).containsKey(channelId))
					this.channel.get(type).put(channelId, new HashMap<String, List<String>>());

				ids.forEach(id -> this.channel.get(type).get(channelId).put(id, new ArrayList<String>(Arrays.asList(permissions))));
				break;

			default:
				break;
			}
		}
	}

	public enum EnumEditOperator {
		ADD,
		REMOVE,
		SET
	}
}