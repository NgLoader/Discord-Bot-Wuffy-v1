package net.wuffy.core.lang;

import java.util.Locale;

public interface II18n {

	public void initialize();
	public void destroy();

	//Translation
	public String format(String key, String locale, String... params);

	//Language
	public ILanguage getLanguage(String locale);

	public ILanguage getLanguage(Locale locale);

	public void addLanguage(String locale);

	public void removeLanguage(String locale);
}