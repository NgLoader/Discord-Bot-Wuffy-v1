package net.wuffy.bot.command.commands;

import java.awt.Color;

public enum MessageType {

	SUCCESS(	4,		new Color(12, 255, 0)),
	LOADING(	-1,		new Color(24, 219, 249)),
	INFO(		60,		new Color(4, 237, 66)),
	PICTURE(	60,		new Color(4, 237, 124)),
	LIST(		180,	new Color(15, 255, 195)),
	HELP(		180,	new Color(178, 255, 207)),
	PERMISSION(	12,		new Color(255, 61, 61)),
	SYNTAX(		12,		new Color(244, 144, 15)),
	ERROR(		8,		new Color(255, 0, 0));

	public int defaultDelay;
	public Color defaultColor;

	private MessageType(int defaultDelay, Color defaultColor) {
		this.defaultDelay = defaultDelay;
		this.defaultColor = defaultColor;
	}
}