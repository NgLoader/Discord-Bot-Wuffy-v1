package de.ngloader.bot.database.guild;

import java.util.List;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.entities.Guild;
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

	public List<String> getPermission(Guild guild, Long channel, User user);

	public void setPermission(Guild guild, Long channel, User user, List<String> permission);

	public void addPermission(Guild guild, Long channel, User user, List<String> permission);

	public void removePermission(Guild guild, Long channel, User user, List<String> permission);

	public boolean hasPermission(Guild guild, Long channel, User user, List<String> permission);

	public List<String> getPermission(Guild guild, Long channel, List<Group> group);

	public void setPermission(Guild guild, Long channel, List<Group> group, List<String> permission);

	public void addPermission(Guild guild, Long channel, List<Group> group, List<String> permission);

	public void removePermission(Guild guild, Long channel, List<Group> group, List<String> permission);

	public boolean hasPermission(Guild guild, Long channel, List<Group> group, List<String> permission);
}