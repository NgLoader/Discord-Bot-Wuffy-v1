package de.ngloader.bot.database.guild;

import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplMemeber;
import net.dv8tion.jda.core.entities.Member;

public class WuffyMember extends ImplMemeber {

	protected final IWuffyExtensionGuild extension;

	protected final WuffyGuild guild;
	protected final WuffyUser user;

	public WuffyMember(Core core, Member member, WuffyGuild guild, IWuffyExtensionGuild extension) {
		super(core, member);

		this.guild = guild;
		this.extension = extension;

		this.user = getUser(WuffyUser.class);
	}

	public String getLocale() {
		var userLocale = this.user.getUserLocale();

		return userLocale != null ? userLocale : this.guild.getGuildLocale();
	}

	@Override
	public WuffyGuild getGuild() {
		return this.guild;
	}

	@Override
	public WuffyUser getUser() {
		return user;
	}
}