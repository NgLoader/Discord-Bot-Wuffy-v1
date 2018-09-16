package net.wuffy.common.util;

import java.util.Locale;

public class LocaleUtil {

	public static Locale getLocaleByTag(String tag) {
		if(tag.length() < 3)
			return null;
		
		String language = tag.substring(0, 2).toLowerCase();
		String country = tag.substring(tag.length() - 2, tag.length()).toUpperCase();

		for(String lang : Locale.getISOLanguages())
			if(language.equals(lang))
				for(String count : Locale.getISOCountries())
					if(country.equals(count))
						return Locale.forLanguageTag(lang + "-" + count);
		return null;
	}
}