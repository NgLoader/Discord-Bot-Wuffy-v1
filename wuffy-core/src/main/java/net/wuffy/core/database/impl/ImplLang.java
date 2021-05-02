package net.wuffy.core.database.impl;

import java.util.Locale;

import net.wuffy.core.Core;

public abstract class ImplLang {

	public abstract String getTranslation(String key);

	protected final Core core;
	protected final Locale locale;

	public ImplLang(Core core, Locale locale) {
		this.core = core;
		this.locale = locale;
	}

	public Core getCore() {
		return this.core;
	}

	public Locale getLocale() {
		return this.locale;
	}
}