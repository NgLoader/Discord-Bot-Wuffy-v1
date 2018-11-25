package net.wuffy.core.lang;

import java.util.Locale;

public interface II18n {

	public void initialize();

	//Translation
	public String format(String key, String locale, String... params);

	public String format(ITranslationKeys key, String locale, String... params);

	public void add(ITranslationKeys key, String locale, String translation);

	public void remove(ITranslationKeys key);

	//Language
	public ILanguage getLanguage(String locale);

	public ILanguage getLanguage(Locale locale);

	public void addLanguage(ILanguage language);

	public void removeLanguage(ILanguage language);
}