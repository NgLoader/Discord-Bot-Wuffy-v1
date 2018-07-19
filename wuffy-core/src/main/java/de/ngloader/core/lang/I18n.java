package de.ngloader.core.lang;

import java.util.HashMap;
import java.util.Map;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.impl.ImplLang;
import de.ngloader.core.logger.Logger;

public class I18n {

	public static String format(Core core, String locale, String key, String... params) {
		return core.getI18n().format(key, locale, params);
	}

	protected Core core;

	protected Map<String, ImplLang> langs = new HashMap<String, ImplLang>();

	public I18n(Core core) {
		this.core = core;
	}

	public void loadLangs(IExtensionLang<?> extensionLang) {
		this.langs.clear();
		extensionLang.getLangs().forEach(lang -> this.langs.put(lang.getLocale().toLanguageTag().toLowerCase(), lang));
		Logger.info("I18n", String.format("Loaded %s translations", Integer.toString(this.langs.size())));
	}

	public String format(String key, String locale, String... params) {
		locale = locale.toLowerCase();

		if(!this.langs.containsKey(locale))
			return String.format("%s_%s", locale.toUpperCase(), key);

		String translation = this.langs.get(locale).getTranslation(key);

		for(int i = 0; (i + 1) < params.length; i++)
			translation = translation.replace(params[i], params[++i]);

		return translation.isEmpty() ? String.format("%s_%s", locale.toUpperCase(), key) : translation.replaceAll("\\\\n", "\n");
	}

	public ImplLang getLang(String locale) {
		return this.langs.get(locale.toLowerCase());
	}

	public <T extends ImplLang> T getLang(Class<T> implLangClass, String locale) {
		return implLangClass.cast(this.langs.get(locale.toLowerCase()));
	}

	public Map<String, ImplLang> getLangs() {
		return langs;
	}

	public Core getCore() {
		return this.core;
	}
}