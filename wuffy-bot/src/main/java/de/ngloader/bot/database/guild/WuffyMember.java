package de.ngloader.bot.database.guild;

import java.util.Arrays;
import java.util.List;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplMemeber;
import net.dv8tion.jda.core.entities.Member;

public class WuffyMember extends ImplMemeber {

	protected IWuffyExtensionGuild extension;

	public WuffyMember(Core core, Member member, IWuffyExtensionGuild extension) {
		super(core, member);
		this.extension = extension;
	}

	public List<String> getPermission(Long channel) {
		return this.extension.getPermission(this.getGuild(), channel, this.getUser());
	}

	public void setPermission(Long channel, String... permission) {
		this.extension.setPermission(this.getGuild(), channel, this.getUser(), Arrays.asList(permission));
	}

	public void addPermission(Long channel, String... permission) {
		this.extension.addPermission(this.getGuild(), channel, this.getUser(), Arrays.asList(permission));
	}

	public void removePermission(Long channel, String... permission) {
		this.extension.removePermission(this.getGuild(), channel, this.getUser(), Arrays.asList(permission));
	}

	public boolean hasPermission(Long channel, String... permission) {
		return this.core.isAdmin(this.getUser()) ||
				this.member.isOwner() ||
				this.extension.hasPermission(this.getGuild(), channel, this.getUser(), Arrays.asList(permission)) ||
				this.extension.hasPermission(this.getGuild(), channel, this.member.getRoles(), Arrays.asList(permission));
	}
}