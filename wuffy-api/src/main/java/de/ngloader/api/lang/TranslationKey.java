package de.ngloader.api.lang;

public enum TranslationKey {

	UNKNOWN("unkown", "Unknown");

	public final String key;

	public final String defaultValue;

	TranslationKey(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}
}