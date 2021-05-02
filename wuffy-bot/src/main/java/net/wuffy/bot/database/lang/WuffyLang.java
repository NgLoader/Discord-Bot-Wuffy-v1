package net.wuffy.bot.database.lang;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.wuffy.core.Core;
import net.wuffy.core.database.impl.ImplLang;

public class WuffyLang extends ImplLang {

	protected Map<String, String> translations = new HashMap<String, String>();

	public WuffyLang(Core core, Locale locale, Map<String, String> translations) {
		super(core, locale);
		this.translations = translations;
	}

	@Override
	public String getTranslation(String key) {
		return this.translations.getOrDefault(key, key);
	}
}