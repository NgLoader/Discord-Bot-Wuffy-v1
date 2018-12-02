package net.wuffy.bot.database.mongo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.DBGuild.EnumPermissionMode;
import net.wuffy.bot.database.info.BanInfo;
import net.wuffy.bot.database.info.BlockedInfo;
import net.wuffy.bot.database.info.MuteInfo;
import net.wuffy.bot.database.info.WarnInfo;

public class MongoGuildCache {

	private static final String DEFAULT_LOCALE = "en-US";

	private static final List<String> DEFAULT_COMMAND_PREFIX = Arrays.asList("~");

	private static final List<EnumPermissionMode> DEFAULT_PERMISSION_MODE = Arrays.asList(EnumPermissionMode.values());

	public MongoGuildCache(long guildId) {
		this._guildId = Long.toString(guildId);
	}

	public MongoGuildCache construct() {
		//Permission
		this.permission = new Permission();
		this.permission.mode = new ArrayList<EnumPermissionMode>(MongoGuildCache.DEFAULT_PERMISSION_MODE);

		this.permission.global = new PermissionGlobal();
		this.permission.global.role = new HashMap<Long, List<String>>();
		this.permission.global.user = new HashMap<Long, List<String>>();

		this.permission.channel = new PermissionChannel();
		this.permission.channel.channel = new HashMap<Long, PermissionGlobal>();

		//Command
		this.command = new Command();
		this.command.prefixes = new ArrayList<>(MongoGuildCache.DEFAULT_COMMAND_PREFIX);

		//Message
		this.message = new Message();
		this.message.delay = new HashMap<MessageType, Integer>();
		this.message.color = new HashMap<MessageType, String>();

		//Log
		this.log = new Log();
		this.log.ban = new ArrayList<BanInfo>();
		this.log.mute = new ArrayList<MuteInfo>();
		this.log.warn = new ArrayList<WarnInfo>();
		this.log.blocked = new ArrayList<BlockedInfo>();

		return this;
	}

	public ObjectId _id;
	public String _guildId;

	public Boolean setup = true;

	public Boolean partner = false;

	public String locale = DEFAULT_LOCALE;

	public BlockedInfo blockedInfo;

	public Permission permission;
	public Command command;
	public Message message;
	public Log log;

	/* Permission */
	public class Permission {
		public List<DBGuild.EnumPermissionMode> mode;

		public PermissionChannel channel;
		public PermissionGlobal global;
	}

	public class PermissionChannel {
		public Map<Long, PermissionGlobal> channel;
	}

	public class PermissionGlobal {
		public Map<Long, List<String>> role;
		public Map<Long, List<String>> user;
	}

	/* Command */
	public class Command {
		public Boolean mention = true;

		public List<String> prefixes;
	}

	/* Message */
	public class Message {
		public Boolean deleteExecuter = true;
		public Boolean deleteBot = true;
	
		public Map<MessageType, Integer> delay;
		public Map<MessageType, String> color;
	}

	/* Log */
	public class Log {
		public List<BanInfo> ban;
		public List<MuteInfo> mute;
		public List<WarnInfo> warn;
		public List<BlockedInfo> blocked;
	}
}