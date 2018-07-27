package de.ngloader.core.command;

import java.awt.Color;

public enum MessageType {

	SUCCESS(new Color(100, 255, 0)),
	LOADING(new Color(75, 255, 15)),
	INFO(new Color(141, 216, 19)),
	LIST(new Color(15, 255, 195)),
	WARN(new Color(255, 231, 14)),
	SYNTAX(new Color(211, 79, 16)),
	ERROR(new Color(255, 0, 0));

	public Color color;

	private MessageType(Color color) {
		this.color = color;
	}
}