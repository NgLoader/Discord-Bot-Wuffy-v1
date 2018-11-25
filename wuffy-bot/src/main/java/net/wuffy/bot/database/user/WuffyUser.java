package net.wuffy.bot.database.user;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.database.BlockedInfo;
import net.wuffy.core.CoreOLD;
import net.wuffy.core.database.impl.ImplUser;

public abstract class WuffyUser extends ImplUser {

	public abstract BlockedInfo getBlocked();

	public abstract void setBlocked(BlockedInfo blockedInfo);

	public abstract String getUserLocale();

	public abstract String getUserLocale(String defaultValue);

	public abstract void setUserLocale(String locale);

	public abstract boolean isAlphaTester();

	public abstract void setAlphaTester(boolean alphaTester);

	public WuffyUser(CoreOLD core, User user) {
		super(core, user);
	}
}