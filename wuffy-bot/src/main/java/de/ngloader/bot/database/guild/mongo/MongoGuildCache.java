package de.ngloader.bot.database.guild.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import de.ngloader.bot.database.BanInfo;
import de.ngloader.bot.database.BlockedInfo;
import de.ngloader.bot.database.MuteInfo;
import de.ngloader.bot.database.WarnInfo;
import de.ngloader.bot.database.guild.WuffyGuild.EnumPermissionMode;
import de.ngloader.bot.database.guild.WuffyGuild.EnumPermissionType;
import de.ngloader.bot.database.guild.WuffyGuild.EnumRoleRankingMode;

public class MongoGuildCache {

	private static final List<String> DEFAULT_PREFIX_LIST = Arrays.asList("~");
	private static final List<EnumPermissionMode> DEFAULT_PERMISSION_MODE_LIST = Arrays.asList(EnumPermissionMode.GLOBAL_USER, EnumPermissionMode.GLOBAL_ROLE);
	private static final EnumRoleRankingMode DEFAULT_RANKING_MODE = EnumRoleRankingMode.DISCORD;

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

	public MongoGuildUserCache user = new MongoGuildUserCache();
	public MongoGuildHistoryCache history = new MongoGuildHistoryCache();
	public MongoGuildRankingCache roleRanking = new MongoGuildRankingCache();
	public MongoGuildPermissionCache permission = new MongoGuildPermissionCache();

	class MongoGuildUserCache {
		public Map<String, BanInfo> bans = new HashMap<String, BanInfo>();
		public Map<String, MuteInfo> mutes = new HashMap<String, MuteInfo>();
		public Map<String, WarnInfo> warns = new HashMap<String, WarnInfo>();
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