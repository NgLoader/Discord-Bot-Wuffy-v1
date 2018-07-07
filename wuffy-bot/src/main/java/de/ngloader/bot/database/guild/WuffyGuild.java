package de.ngloader.bot.database.guild;

import java.util.List;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplGuild;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class WuffyGuild extends ImplGuild {

	protected IWuffyExtensionGuild extension;

	public WuffyGuild(Core core, Guild guild, IWuffyExtensionGuild extension) {
		super(core, guild);
		this.extension = extension;
	}

	public boolean hasPermission(Channel channel, User user) {
		return false;
	}

	public String getLocale() {
		return this.extension.getLocale(this.guild);
	}

	public void setLocale(String locale) {
		this.extension.setLocale(this.guild, locale);
	}

	public List<String> getPrefixes() {
		return this.extension.getPrefixes(this.guild);
	}

	public void addPrefix(String prefix) {
		this.extension.addPrefix(this.guild, prefix);
	}

	public void removePrefix(String prefix) {
		this.extension.removePrefix(this.guild, prefix);
	}

	public void setPrefixes(List<String> prefixes) {
		this.extension.setPrefixes(this.guild, prefixes);
	}

	public boolean isMention() {
		return this.extension.isMention(this.guild);
	}

	public void setMention(boolean mention) {
		this.extension.setMention(this.guild, mention);
	}

	public String getInvite() {
		return this.extension.getInvite(this.guild);
	}

	public void setInvite(String invite) {
		this.extension.setInvite(this.guild, invite);
	}

	public List<String> getPermission(Long channel, User user) {
		return this.extension.getPermission(this.guild, channel, user);
	}

	public void setPermission(Long channel, User user, List<String> permission) {
		this.extension.setPermission(this.guild, channel, user, permission);
	}

	public void addPermission(Long channel, User user, List<String> permission) {
		this.extension.addPermission(this.guild, channel, user, permission);
	}

	public void removePermission(Long channel, User user, List<String> permission) {
		this.extension.removePermission(this.guild, channel, user, permission);
	}

	public boolean hasPermission(Long channel, User user, List<String> permission) {
		return this.extension.hasPermission(this.guild, channel, user, permission);
	}

	public List<String> getPermission(Long channel, List<Group> group) {
		return this.extension.getPermission(this.guild, channel, group);
	}

	public void setPermission(Long channel, List<Group> group, List<String> permission) {
		this.extension.setPermission(this.guild, channel, group, permission);
	}

	public void addPermission(Long channel, List<Group> group, List<String> permission) {
		this.extension.addPermission(this.guild, channel, group, permission);
	}

	public void removePermission(Long channel, List<Group> group, List<String> permission) {
		this.extension.removePermission(this.guild, channel, group, permission);
	}

	public boolean hasPermission(Long channel, List<Group> group, List<String> permission) {
		return this.extension.hasPermission(this.guild, channel, group, permission);
	}
}