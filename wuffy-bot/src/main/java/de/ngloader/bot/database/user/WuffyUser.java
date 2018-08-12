package de.ngloader.bot.database.user;

import de.ngloader.bot.database.BlockedInfo;
import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplUser;
import net.dv8tion.jda.core.entities.User;

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