package net.wuffy.bot.database.guild;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.core.CoreOLD;
import net.wuffy.core.database.impl.ImplGuild;
import net.wuffy.core.database.impl.ImplMember;
import net.wuffy.core.database.impl.ImplUser;

public abstract class WuffyMember extends ImplMember {

	public WuffyMember(CoreOLD core, ImplUser user, Member member, ImplGuild guild) {
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

		return userLocale != null && !userLocale.isEmpty() ? userLocale : this.getGuild().getGuildLocale();
	}

	public boolean hasPermission(Channel channel, PermissionKeys... permissions) {
		return this.getGuild().hasPermission(channel, this.member, permissions);
	}
}