package de.ngloader.core.util;

import java.util.ArrayList;
import java.util.List;

public class CastUtil {

	private static final List<String> EMPTY_STRING_LIST = new ArrayList<String>();

	@SuppressWarnings("unchecked") //TODO fix unchecked
	public static List<String> castToStringList(Object object) {
		return EMPTY_STRING_LIST.getClass().cast(object);
	}
}