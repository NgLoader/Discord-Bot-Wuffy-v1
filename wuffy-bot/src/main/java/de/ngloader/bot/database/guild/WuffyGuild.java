package de.ngloader.bot.database.guild;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.ngloader.bot.database.BanInfo;
import de.ngloader.bot.database.BlockedInfo;
import de.ngloader.bot.database.MuteInfo;
import de.ngloader.bot.database.WarnInfo;
import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplGuild;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

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
	public abstract boolean isMention();

	public abstract void setMention(boolean mention);

	// Disabled Commands
	public abstract List<String> getDisabledCommands();

	public abstract void addDisabledCommands(String command);

	public abstract void removeDisabledCommands(String command);

	public abstract void setDisabledCommands(List<String> commands);

	// Blocked
	public abstract BlockedInfo getBlocked();

	public abstract void setBlocked(BlockedInfo blockedInfo);

	// Ban
	public abstract BanInfo getBan(Long userId);

	public abstract void setBan(Long userId, BanInfo banInfo);

	// Mute
	public abstract MuteInfo getMute(Long userId);

	public abstract void setMute(Long userId, MuteInfo muteInfo);

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

	public boolean hasPermission(Channel channel, User user, String... permissions) {
		return this.hasPermission(channel, this.getMember(user), permissions);
	}

	public boolean hasPermission(Channel channel, Member member, String... permissions) {
		if(member.isOwner() || this.core.isAdmin(member.getUser()))
			return true;

		List<Role> roles = member.getRoles();
		List<String> permissionList = Arrays.asList(permissions);
		List<EnumPermissionMode> permissionMode = this.getPermissionMode();

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
		WUFFY
	}

	public enum EnumPermissionType {
		USER,
		ROLE,
		RANKING
	}

	public enum EnumPermissionMode {
		GLOBAL_USER,
		GLOBAL_ROLE,
		GLOBAL_RANKING,

		CHANNEL_USER,
		CHANNEL_ROLE,
		CHANNEL_RANKING,
	}
}