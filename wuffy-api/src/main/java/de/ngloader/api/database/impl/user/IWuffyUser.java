package de.ngloader.api.database.impl.user;

import net.dv8tion.jda.core.entities.User;

public interface IWuffyUser extends User {

	public boolean isBlocked();

	public void setBlocked(boolean blocked, String reason, Long expire);

	public String getLocale();

	public void setLocale(String locale);
}