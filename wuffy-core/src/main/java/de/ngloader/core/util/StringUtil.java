package de.ngloader.core.util;

public class StringUtil {

	public static String writeFirstUpperCase(String message) {
		return message.substring(0, 1).toUpperCase() + message.substring(1).toLowerCase();
	}
}