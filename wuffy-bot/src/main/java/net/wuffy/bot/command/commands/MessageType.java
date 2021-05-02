package net.wuffy.bot.command.commands;

import java.awt.Color;

public enum MessageType {

	SUCCESS(new Color(12, 255, 0)),
	LOADING(new Color(24, 219, 249)),
	INFO(new Color(4, 237, 66)),
	PICTURE(new Color(4, 237, 124)),
	LIST(new Color(15, 255, 195)),
	HELP(new Color(178, 255, 207)),
	PERMISSION(new Color(255, 61, 61)),
	SYNTAX(new Color(244, 144, 15)),
	ERROR(new Color(255, 0, 0));

	public Color color;

	private MessageType(Color color) {
		this.color = color;
	}
}