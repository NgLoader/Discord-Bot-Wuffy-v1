package de.ngloader.bot.database.guild;

import java.util.List;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

public interface IWuffyExtensionGuild {

	public String getLocale(Guild guild);

	public void setLocale(Guild guild, String locale);

	public List<String> getPrefixes(Guild guild);

	public void addPrefix(Guild guild, String prefix);

	public void removePrefix(Guild guild, String prefix);

	public void setPrefixes(Guild guild, List<String> prefixes);

	public boolean isMention(Guild guild);

	public void setMention(Guild guild, boolean mention);

	public String getInvite(Guild guild);

	public void setInvite(Guild guild, String invite);

	public List<BanHistoryInformation> getBanHistory(Guild guild);
	
	public void addBanHistory(Guild guild, BanHistoryInformation document);

	public void removeBanHistory(Guild guild, BanHistoryInformation document);

	public void setBanHistory(Guild guild, List<BanHistoryInformation> documents);

	public List<MuteHistoryInformation> getMuteHistory(Guild guild);
	
	public void addMuteHistory(Guild guild, MuteHistoryInformation document);

	public void removeMuteHistory(Guild guild, MuteHistoryInformation document);

	public void setMuteHistory(Guild guild, List<MuteHistoryInformation> documents);

	public List<WarnHistoryInformation> getWarnHistory(Guild guild);
	
	public void addWarnHistory(Guild guild, WarnHistoryInformation document);

	public void removeWarnHistory(Guild guild, WarnHistoryInformation document);

	public void setWarnHistory(Guild guild, List<WarnHistoryInformation> documents);

	public AutoPurneSettings getAutoPurne(Guild guild);

	public void updateAutoPurne(Guild guild, String key, Object value);

	public void setAutoPurne(Guild guild, AutoPurneSettings settings);

	public Map<Long, Integer> getRoleRanking(Guild guild);

	public void updateRoleRanking(Guild guild, Long role, Integer ranking);

	public void setRoleRanking(Guild guild, Map<Long, Integer> ranking);

	public List<String> getUserPermission(Guild guild, Long channel, User user);

	public void setUserPermission(Guild guild, Long channel, User user, String... permission);

	public void addUserPermission(Guild guild, Long channel, User user, String... permission);

	public void removeUserPermission(Guild guild, Long channel, User user, String... permission);

	public List<String> getRolePermission(Guild guild, Long channel, String... roles);

	public void setRolePermission(Guild guild, Long channel, List<Role> roles, String... permission);

	public void addRolePermission(Guild guild, Long channel, List<Role> roles, String... permission);

	public void removeRolePermission(Guild guild, Long channel, List<Role> roles, String... permission);

	public List<String> getUserGlobalPermission(Guild guild, User user);

	public void setUserGlobalPermission(Guild guild, User user, String... permission);

	public void addUserGlobalPermission(Guild guild, User user, String... permission);

	public void removeUserGlobalPermission(Guild guild, User user, String... permission);

	public List<String> getRoleGlobalPermission(Guild guild, String... roles);

	public void setRoleGlobalPermission(Guild guild, List<Role> roles, String... permission);

	public void addRoleGlobalPermission(Guild guild, List<Role> roles, String... permission);

	public void removeRoleGlobalPermission(Guild guild, List<Role> roles, String... permission);

	public class WarnHistoryInformation {

		public String reason;

		public String date;

		public Integer points;

		public Long warnedLongId;
		public Long warnedByLongId;
	}

	public class BanHistoryInformation {

		public String reason;

		public String date;

		public Long bannedLongId;
		public Long bannedByLongId;
	}

	public class MuteHistoryInformation {

		public String reason;

		public String date;

		public Long mutedLongId;
		public Long mutedByLongId;
	}

	public class AutoPurneSettings {

		public boolean enabled;
	}
}