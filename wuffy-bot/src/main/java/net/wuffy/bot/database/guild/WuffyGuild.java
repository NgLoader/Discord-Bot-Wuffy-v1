package net.wuffy.bot.database.guild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.BanInfo;
import net.wuffy.bot.database.BlockedInfo;
import net.wuffy.bot.database.MuteInfo;
import net.wuffy.bot.database.NotificationInfo;
import net.wuffy.bot.database.NotificationType;
import net.wuffy.bot.database.WarnInfo;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.core.Core;
import net.wuffy.core.database.impl.ImplGuild;

public abstract class WuffyGuild extends ImplGuild {

	// Locale
	public abstract String getGuildLocale();

	public abstract void setGuildLocale(String locale);

	// Prefix
	public abstract List<String> getPrefixes();

	public abstract void addPrefix(String prefix);

	public abstract void removePrefix(String prefix);

	public abstract void setPrefixes(List<String> prefixes);

	// Mention
	public abstract Boolean isMention();

	public abstract void setMention(boolean mention);

	// Disabled Commands
	public abstract List<String> getDisabledCommands();

	public abstract void addDisabledCommands(String command);

	public abstract void removeDisabledCommands(String command);

	public abstract void setDisabledCommands(List<String> commands);

	// Blocked
	public abstract BlockedInfo getBlocked();

	public abstract void setBlocked(BlockedInfo blockedInfo);

	// Warn
	public abstract WarnInfo getWarn(Long userId);

	public abstract void setWarn(Long userId, WarnInfo warnInfo);

	// BlockedHistory
	public abstract List<BlockedInfo> getBlockedHistory();

	public abstract void addBlockedHistory(BlockedInfo blockedInfo);

	public abstract void removeBlockedHistory(BlockedInfo blockedInfo);

	public abstract void setBlockedHistory(List<BlockedInfo> blockedInfos);

	// BanHistory
	public abstract List<BanInfo> getBanHistory();

	public abstract void addBanHistory(BanInfo banInfo);

	public abstract void removeBanHistory(BanInfo banInfo);

	public abstract void setBanHistory(List<BanInfo> banInfos);

	// MuteHistory
	public abstract List<MuteInfo> getMuteHistory();

	public abstract void addMuteHistory(MuteInfo muteInfo);

	public abstract void removeMuteHistory(MuteInfo muteInfo);

	public abstract void setMuteHistory(List<MuteInfo> muteInfos);

	// WarnHistory
	public abstract List<WarnInfo> getWarnHistory();

	public abstract void addWarnHistory(WarnInfo warnInfo);

	public abstract void removeWarnHistory(WarnInfo warnInfo);

	public abstract void setWarnHistory(List<WarnInfo> warnInfos);

	// RoleRanking
	public abstract Map<Long, Integer> getRoleRanking();

	public abstract void setRoleRanking(Long role, Integer ranking);

	public abstract void setRoleRanking(Object... values);

	public abstract void setRoleRanking(Map<Long, Integer> ranking);

	public abstract EnumRoleRankingMode getRoleRankingMode();

	public abstract void setRoleRankingMode(EnumRoleRankingMode rankingMode);

	// PermissionMode
	public abstract List<EnumPermissionMode> getPermissionMode();

	public abstract void addPermissionMode(EnumPermissionMode... permissionMode);

	public abstract void removePermissionMode(EnumPermissionMode... permissionMode);

	public abstract void setPermissionMode(List<EnumPermissionMode> permissionModeList);

	// Permission Channel
	public abstract List<String> getPermissionChannel(EnumPermissionType type, Long channelId, String... ids);

	public abstract void setPermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions);

	public abstract void setPermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions);

	public abstract void addPermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions);

	public abstract void addPermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions);

	public abstract void removePermissionChannel(EnumPermissionType type, Long channelId, String id, String... permissions);

	public abstract void removePermissionChannel(EnumPermissionType type, Long channelId, List<String> ids, String... permissions);

	// Permission Global
	public abstract List<String> getPermissionGlobal(EnumPermissionType type, String... ids);

	public abstract void setPermissionGlobal(EnumPermissionType type, String id, String... permissions);

	public abstract void setPermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions);

	public abstract void addPermissionGlobal(EnumPermissionType type, String id, String... permissions);

	public abstract void addPermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions);

	public abstract void removePermissionGlobal(EnumPermissionType type, String id, String... permissions);

	public abstract void removePermissionGlobal(EnumPermissionType type, List<String> ids, String... permissions);

	//AutoPrune
	public abstract Boolean isAutoPrune();

	public abstract void setAutoPrune(Boolean autoPrune);

	public abstract Integer getAutoPruneDeleteDays();

	public abstract void setAutoPruneDeleteDays(Integer days);

	//Delete Messages
	public abstract Boolean isMessageDeleteExecuter();

	public abstract void setMessageDeleteExecuter(Boolean delete);

	public abstract Boolean isMessageDeleteBot();

	public abstract void setMessageDeleteBot(Boolean delete);

	public abstract Boolean isMessageDeleteDelay(MessageType type);

	public abstract Integer getMessageDeleteDelay(MessageType type);

	public abstract Map<MessageType, Integer> getMessageDeleteDelays();

	public abstract void setMessageDeleteDelay(MessageType type, Integer delay);

	public abstract void removeMessageDeleteDelay(MessageType type);

	public abstract String getMessageColorCode(MessageType type);

	public abstract Map<MessageType, String> getMessageColorCodes();

	public abstract void setMessageColorCode(MessageType type, String colorCode);

	//Notifications
	public abstract List<NotificationInfo> getNotifications(NotificationType type);

	public abstract NotificationInfo getNotification(NotificationType type, String key);

	public abstract void addNotification(NotificationType type, NotificationInfo notificationInfo);

	public abstract void updateNotification(NotificationType type, String key, NotificationInfo info);

	public abstract void removeNotification(NotificationType type, String key);

	public abstract void setNotificationMessage(NotificationType type, String key, String message);

	public abstract void setNotification(NotificationType type, List<NotificationInfo> notificationInfos);

	//AutoRole
	public abstract List<String> getAutoRole();

	public abstract void addAutoRole(String role);

	public abstract void removeAutoRole(String role);

	public abstract void setAutoRole(List<String> roles);

	public WuffyGuild(Core core, Guild guild) {
		super(core, guild);
	}

	public boolean hasHighestRole(User userIsHigher, Member memberIsLower) {
		return this.hasHighestRole(this.getMember(userIsHigher), memberIsLower, this.getRoleRankingMode() == EnumRoleRankingMode.WUFFY);
	}

	public boolean hasHighestRole(Member memberIsHigher, User userIsLower) {
		return this.hasHighestRole(memberIsHigher, this.getMember(userIsLower), this.getRoleRankingMode() == EnumRoleRankingMode.WUFFY);
	}

	public boolean hasHighestRole(User userIsHigher, User userIsLower) {
		return this.hasHighestRole(this.getMember(userIsHigher), this.getMember(userIsLower), this.getRoleRankingMode() == EnumRoleRankingMode.WUFFY);
	}

	public boolean hasHighestRole(Member memberIsHigher, Member memberIsLower) {
		return this.hasHighestRole(memberIsHigher, memberIsLower, this.getRoleRankingMode() == EnumRoleRankingMode.WUFFY);
	}

	public boolean hasHighestRole(User userIsHigher, Member memberIsLower, boolean equals) {
		return this.hasHighestRole(this.getMember(userIsHigher), memberIsLower, equals);
	}

	public boolean hasHighestRole(Member memberIsHigher, User userIsLower, boolean equals) {
		return this.hasHighestRole(memberIsHigher, this.getMember(userIsLower), equals);
	}

	public boolean hasHighestRole(User userIsHigher, User userIsLower, boolean equals) {
		return this.hasHighestRole(this.getMember(userIsHigher), this.getMember(userIsLower), equals);
	}

	public boolean hasHighestRole(Member memberIsHigher, Member memberIsLower, boolean equals) {
		boolean higherIsAdmin = this.core.isAdmin(memberIsHigher.getUser());
		boolean lowerIsAdmin = this.core.isAdmin(memberIsLower.getUser());

		if(higherIsAdmin || lowerIsAdmin)
			return equals ? higherIsAdmin && lowerIsAdmin : higherIsAdmin;

		if(memberIsHigher.isOwner() || memberIsLower.isOwner())
			return equals ? memberIsHigher.isOwner() && memberIsLower.isOwner() : memberIsHigher.isOwner();

		Integer memberHighest;
		Integer memberHighest2;

		EnumRoleRankingMode roleRankingMode = this.getRoleRankingMode();

		if(roleRankingMode == EnumRoleRankingMode.WUFFY) {
			Map<Long, Integer> roleRanking = this.getRoleRanking();

			memberHighest = memberIsHigher.getRoles().stream()
					.map(role -> roleRanking.get(role.getIdLong()))
					.filter(roleIdLong -> roleIdLong != null)
					.collect(Collectors.summarizingInt(Integer::intValue))
					.getMax();
			memberHighest2 = memberIsLower.getRoles().stream()
					.map(role -> roleRanking.get(role.getIdLong()))
					.filter(roleIdLong -> roleIdLong != null)
					.collect(Collectors.summarizingInt(Integer::intValue))
					.getMax();
		} else {
			memberHighest = memberIsHigher.getRoles().stream().map(role -> role.getPosition()).collect(Collectors.summarizingInt(Integer::intValue)).getMax();
			memberHighest2 = memberIsLower.getRoles().stream().map(role -> role.getPosition()).collect(Collectors.summarizingInt(Integer::intValue)).getMax();
		}

		return memberHighest > memberHighest2 || (equals && memberHighest == memberHighest2);
	}

	public boolean hasPermission(MessageChannel channel, User user, PermissionKeys... permissions) {
		return this.hasPermission(channel, this.getMember(user), permissions);
	}

	public boolean hasPermission(MessageChannel channel, Member member, PermissionKeys... permissions) {
		if(member.isOwner() || this.core.isAdmin(member.getUser()) || permissions.length == 0)
			return true;

		List<Role> roles = new ArrayList<Role>(member.getRoles());
		roles.add(member.getGuild().getPublicRole()); //Add @everyone role (not contains by $.getRoles())

		List<String> permissionList = Arrays.asList(permissions).stream()
				.map(permission -> permission.key)
				.collect(Collectors.toList());
		List<EnumPermissionMode> permissionMode = this.getPermissionMode();

		//Add category permissions
		permissionList.addAll(Arrays.asList(permissions).stream()
				.map(permission -> permission.category.key)
				.distinct()
				.collect(Collectors.toList()));

		String userIdString = null;
		String[] userRoleIds = null;
		String[] rankingIds = null;

		//GlobalRanking
		if(permissionMode.contains(EnumPermissionMode.GLOBAL_RANKING)) {
			if(this.getPermissionGlobal(EnumPermissionType.RANKING,
					rankingIds == null ? (rankingIds = this.getRoleRanking().entrySet().stream()
						.filter(entry -> roles.stream().anyMatch(role -> role.getIdLong() == entry.getKey()))
						.map(entry -> Integer.toString(entry.getValue())).toArray(String[]::new)) : rankingIds)
					.stream()
					.anyMatch(perm -> permissionList.contains(perm)))
				return true;
		}

		//GlobalRole
		if(permissionMode.contains(EnumPermissionMode.GLOBAL_ROLE))
			if(this.getPermissionGlobal(EnumPermissionType.ROLE,
					userRoleIds == null ? (userRoleIds = roles.stream().map(role -> Long.toString(role.getIdLong())).toArray(String[]::new)) : userRoleIds)
					.stream()
					.anyMatch(perm -> permissionList.contains(perm)))
				return true;

		//GlobalUser
		if(permissionMode.contains(EnumPermissionMode.GLOBAL_USER))
			if(this.getPermissionGlobal(
					EnumPermissionType.USER,
					userIdString == null ? (userIdString = Long.toString(member.getUser().getIdLong())) : userIdString)
				.stream()
				.anyMatch(perm -> permissionList.contains(perm)))
				return true;

		Long channelIdLong = channel.getIdLong();

		//Ranking
		if(permissionMode.contains(EnumPermissionMode.CHANNEL_RANKING))
			if(this.getPermissionChannel(EnumPermissionType.RANKING,
					channelIdLong,
					rankingIds == null ? (rankingIds = this.getRoleRanking().entrySet().stream()
						.filter(entry -> roles.stream().anyMatch(role -> role.getIdLong() == entry.getKey()))
						.map(entry -> Integer.toString(entry.getValue())).toArray(String[]::new)) : rankingIds)
					.stream()
					.anyMatch(perm -> permissionList.contains(perm)))
				return true;

		//Role
		if(permissionMode.contains(EnumPermissionMode.CHANNEL_ROLE))
			if(this.getPermissionChannel(EnumPermissionType.ROLE,
					channelIdLong,
					userRoleIds == null ? (userRoleIds = roles.stream().map(role -> Long.toString(role.getIdLong())).toArray(String[]::new)) : userRoleIds)
					.stream()
					.anyMatch(perm -> permissionList.contains(perm)))
				return true;

		//User
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

	public enum EnumRoleRankingMode {
		DISCORD,
		WUFFY;

		public static EnumRoleRankingMode search(String search) {
			for(EnumRoleRankingMode mode : EnumRoleRankingMode.values())
				if(mode.name().equalsIgnoreCase(search))
					return mode;

			return null;
		}
	}

	public enum EnumPermissionType {
		USER,
		ROLE,
		RANKING;

		public static EnumPermissionType search(String search) {
			for(EnumPermissionType type : EnumPermissionType.values())
				if(type.name().equalsIgnoreCase(search))
					return type;

			return null;
		}
	}

	public enum EnumPermissionMode {
		GLOBAL_USER,
		GLOBAL_ROLE,
		GLOBAL_RANKING,

		CHANNEL_USER,
		CHANNEL_ROLE,
		CHANNEL_RANKING;

		public static EnumPermissionMode search(String search) {
			for(EnumPermissionMode mode : EnumPermissionMode.values())
				if(mode.name().equalsIgnoreCase(search))
					return mode;

			return null;
		}
	}
}