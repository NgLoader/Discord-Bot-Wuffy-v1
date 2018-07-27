package de.ngloader.bot.database;

public class NotificationInfo {

	public String name;
	public String webhook;
	public String message;

	public NotificationInfo(String name, String webhook, String message) {
		this.name = name;
		this.webhook = webhook;
		this.message = message;
	}
}