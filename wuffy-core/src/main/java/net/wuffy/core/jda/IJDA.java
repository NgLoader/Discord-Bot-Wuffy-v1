package net.wuffy.core.jda;

import net.dv8tion.jda.core.hooks.EventListener;

public interface IJDA {

	public void login();

	public void addListener(EventListener listener);

	public void logout();
}