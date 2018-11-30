package net.wuffy.core.lang;

import java.util.Locale;

public interface ILanguage {

	public Locale getLocale();

	public String getTranslation(String key);

	public String getTranslation(ITranslationKeys key);

	public void addTranslation(ITranslationKeys key, String translation);

	public void removeTranslation(ITranslationKeys key);
}