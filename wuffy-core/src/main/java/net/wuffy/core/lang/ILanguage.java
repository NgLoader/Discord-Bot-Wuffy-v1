package net.wuffy.core.lang;

import java.util.Locale;

public interface ILanguage {

	public Locale getLocale();

	public void setLocale(Locale locale);

	public String getTranslation(String key);

	public void addTranslation(String key, String translation);

	public void removeTranslation(String key);
}