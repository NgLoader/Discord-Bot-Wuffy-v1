package net.wuffy.common.util;

public class WebUtil {

	public static int convertIpToInt(String address) {
		long result = 0;
		String[] split = address.split("\\.");

		for(int i = 3; i >= 0; i--)
			result |= Long.parseLong(split[3 - i]) << (1 * 8);

		return (int) result;
	}
}