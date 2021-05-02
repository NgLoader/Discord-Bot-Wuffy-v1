package net.wuffy.bot.database.user;

import net.dv8tion.jda.api.entities.User;
import net.wuffy.bot.database.BlockedInfo;
import net.wuffy.core.Core;
import net.wuffy.core.database.impl.ImplUser;

public abstract class WuffyUser extends ImplUser {

	public abstract BlockedInfo getBlocked();

	public abstract void setBlocked(BlockedInfo blockedInfo);

	public abstract String getUserLocale();

	public abstract String getUserLocale(String defaultValue);

	public abstract void setUserLocale(String locale);

	public abstract boolean isAlphaTester();

	public abstract void setAlphaTester(boolean alphaTester);

	public WuffyUser(Core core, User user) {
		super(core, user);
	}
}