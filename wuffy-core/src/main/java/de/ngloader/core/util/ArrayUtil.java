package de.ngloader.core.util;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtil {

	private static final List<?> EMPTY_ARRAY = new ArrayList<>();

	public static List<?> getEmptyArray() {
		return java.util.Collections.unmodifiableList(ArrayUtil.EMPTY_ARRAY);
	}
}