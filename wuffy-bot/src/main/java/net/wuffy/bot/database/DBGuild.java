package net.wuffy.bot.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.wuffy.bot.Wuffy;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.core.Core;
import net.wuffy.core.event.impl.EventGuild;

public abstract class DBGuild extends EventGuild<DBUser, DBMember> {

	/* Mention */
	public abstract boolean isMention();
	public abstract void setMention(boolean mention);

	/* Prefix */
	public abstract List<String> getPrefixes();
	public abstract void addPrefix(String prefix);
	public abstract void removePrefix(String prefix);
	public abstract void setPrefix(String... prefix);

	/* Message */
	public abstract boolean isDeleteExecuterMessage();
	public abstract void setDeleteExecuterMessage(boolean delete);

	public abstract boolean isDeleteBotMessage();
	public abstract void setDeleteBotMessage(boolean delete);

	/* Permission */
	public abstract List<EnumPermissionMode> getPermissionMode();
	public abstract void addPermissionMode(EnumPermissionMode... permissionMode);
	public abstract void removePermissionMode(EnumPermissionMode... permissionMode);
	public abstract void setPermissionMode(List<EnumPermissionMode> permissionModeList);

	/* Permission Channel */
	public abstract List<String> getPermissionChannel(EnumPermissionType type, Long channelId, String... ids);

	public abstract void setPermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions);
	public abstract void setPermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions);

	public abstract void addPermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions);
	public abstract void addPermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions);

	public abstract void removePermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions);
	public abstract void removePermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions);

	/* Permission Global */
	public abstract List<String> getPermissionGlobal(EnumPermissionType type, String... ids);

	public abstract void setPermissionGlobal(EnumPermissionType type, String id, String... permissions);
	public abstract void setPermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions);

	public abstract void addPermissionGlobal(EnumPermissionType type, String id, String... permissions);
	public abstract void addPermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions);

	public abstract void removePermissionGlobal(EnumPermissionType type, String id, String... permissions);
	public abstract void removePermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions);

	/* Partner */
	public abstract Boolean isPartner();
	public abstract void setPartner(boolean partner);

	/* Database */
	public abstract void deleteFromDatabase();

	/* Handler */
	public abstract void destroy();

	public DBGuild(Core core, Guild guild) {
		super(core, guild);
	}

	@Override
	public Wuffy getCore() {
		return (Wuffy) this.core;
	}

	/* Permission */
	public boolean hasPermission(DBMember member, PermissionKeys... permissions) {
		return this.hasPermission(null,  member, permissions);
	}

	public boolean hasPermission(Channel channel, DBMember  member, PermissionKeys... permissions) {
		if(permissions.length == 0 || member.isOwner() || this.core.getConfig().admins.contains(member.getUser().getIdLong()))
			return true;

		List<Role> roles = new ArrayList<Role>(member.getRoles());
		roles.add(member.getGuild().getPublicRole());

		List<EnumPermissionMode> permissionMode = this.getPermissionMode();

		List<PermissionKeys> permissionKeyList = Arrays.asList(permissions);

		//Add permission keys
		List<String> permissionList = permissionKeyList.stream()
				.map(permission -> permission.key)
				.collect(Collectors.toList());

		//Add permission category keys
		permissionList.addAll(permissionKeyList.stream()
				.map(permission -> permission.category.key)
				.distinct()
				.collect(Collectors.toList()));

		String userIdString = null;
		String[] userRoleIds = null;

		//Global Role
		if(permissionMode.contains(EnumPermissionMode.GLOBAL_ROLE))
			if(this.getPermissionGlobal(EnumPermissionType.ROLE,
					userRoleIds == null ? (userRoleIds = roles.stream().map(role -> Long.toString(role.getIdLong())).toArray(String[]::new)) : userRoleIds)
					.stream()
					.anyMatch(perm -> permissionList.contains(perm)))
				return true;

		//Global User
		if(permissionMode.contains(EnumPermissionMode.GLOBAL_USER))
			if(this.getPermissionGlobal(
					EnumPermissionType.USER,
					userIdString == null ? (userIdString = Long.toString(member.getUser().getIdLong())) : userIdString)
				.stream()
				.anyMatch(perm -> permissionList.contains(perm)))
				return true;

		Long channelIdLong = channel.getIdLong();

		//Channel Role
		if(permissionMode.contains(EnumPermissionMode.CHANNEL_ROLE))
			if(this.getPermissionChannel(EnumPermissionType.ROLE,
					channelIdLong,
					userRoleIds == null ? (userRoleIds = roles.stream().map(role -> Long.toString(role.getIdLong())).toArray(String[]::new)) : userRoleIds)
					.stream()
					.anyMatch(perm -> permissionList.contains(perm)))
				return true;

		//Channel User
		if(permissionMode.contains(EnumPermissionMode.CHANNEL_USER))
			if(this.getPermissionChannel(
						EnumPermissionType.USER,
						channelIdLong,
						userIdString == null ? (userIdString = Long.toString(member.getUser().getIdLong())) : userIdString)
					.stream()
					.anyMatch(perm -> permissionList.contains(perm)))
				return true;
		return false;
	}

	public enum EnumPermissionType {
		USER,
		ROLE;

		public static EnumPermissionType search(String search) {
			for(EnumPermissionType mode : EnumPermissionType.values())
				if(mode.name().equalsIgnoreCase(search))
					return mode;
			return null;
		}
	}

	public enum EnumPermissionMode {
		GLOBAL_USER,
		GLOBAL_ROLE,

		CHANNEL_USER,
		CHANNEL_ROLE;

		public static EnumPermissionMode search(String search) {
			for(EnumPermissionMode mode : EnumPermissionMode.values())
				if(mode.name().equalsIgnoreCase(search))
					return mode;
			return null;
		}
	}
}