package net.wuffy.common.util;

import java.util.EnumSet;

public class EnumUtil {

	public static <T extends Enum<T>> boolean contains(EnumSet<T> es1, EnumSet<T> es2) {
		for(T e : es1)
			if(es2.contains(e))
				return true;
		return false;
	}

	public static <T extends Enum<T>> boolean containsAll(EnumSet<T> es1, EnumSet<T> es2) {
		return es1.containsAll(es2);
	}
}