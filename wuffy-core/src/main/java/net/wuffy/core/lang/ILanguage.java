package net.wuffy.core.lang;

import java.util.Locale;

public interface ILanguage {

	public Locale getTranslation(String key);

	public Locale getTranslation(ITranslationKeys key);

	public void addTranslation(ITranslationKeys key, String translation);

	public void removeTranslation(ITranslationKeys key);
}