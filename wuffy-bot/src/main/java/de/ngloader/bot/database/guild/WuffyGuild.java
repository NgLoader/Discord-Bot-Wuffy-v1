package de.ngloader.bot.database.guild;

import java.util.List;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplGuild;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class WuffyGuild extends ImplGuild {

	protected final IWuffyExtensionGuild extension;

	public WuffyGuild(Core core, Guild guild, IWuffyExtensionGuild extension) {
		super(core, guild);
		this.extension = extension;
	}

	public boolean hasPermission(Channel channel, User user) {
		return false;
	}

	public String getGuildLocale() {
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
}