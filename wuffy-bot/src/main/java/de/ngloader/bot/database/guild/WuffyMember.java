package de.ngloader.bot.database.guild;

import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplGuild;
import de.ngloader.core.database.impl.ImplMember;
import de.ngloader.core.database.impl.ImplUser;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;

public abstract class WuffyMember extends ImplMember {

	public WuffyMember(Core core, ImplUser user, Member member, ImplGuild guild) {
		super(core, user, member, guild);
	}

	@Override
	public WuffyGuild getGuild() {
		return WuffyGuild.class.cast(this.guild);
	}

	@Override
	public WuffyUser getUser() {
		return WuffyUser.class.cast(this.user);
	}

	public String getLocale() {
		var userLocale = this.getUser().getUserLocale();

		return userLocale != null ? userLocale : this.getGuild().getGuildLocale();
	}

	public boolean hasPermission(Channel channel, String... permissions) {
		return this.getGuild().hasPermission(channel, this.member, permissions);
	}
}