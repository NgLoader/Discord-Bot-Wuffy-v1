package de.ngloader.core.jda;

import net.dv8tion.jda.core.hooks.EventListener;

public interface IJDAAdapter {

	public void login();

	public void addListener(EventListener listener);

	public void logout();
}