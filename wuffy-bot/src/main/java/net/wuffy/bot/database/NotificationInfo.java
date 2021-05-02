package net.wuffy.bot.database;

public class NotificationInfo {

	public String name;
	public String webhook;
	public String message;
	public String channelId;

	public NotificationInfo(String name, String webhook, String message) {
		this.name = name;
		this.webhook = webhook;
		this.message = message;
	}

	public NotificationInfo(String name, String webhook, String message, String channelId) {
		this.name = name;
		this.webhook = webhook;
		this.message = message;
		this.channelId = channelId;
	}
}