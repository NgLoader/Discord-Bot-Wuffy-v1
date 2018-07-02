package de.ngloader.bot.database.lang;

import java.util.Locale;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplLang;

public class WuffyLang extends ImplLang {

	public WuffyLang(Core core, Locale locale) {
		super(core, locale);
	}

	@Override
	public String getTranslation(String key) {
		return null;
	}
}