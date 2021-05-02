package net.wuffy.core.jda;

import net.dv8tion.jda.api.hooks.EventListener;

public interface IJDAAdapter {

	public void login();

	public void addListener(EventListener listener);

	public void logout();
}