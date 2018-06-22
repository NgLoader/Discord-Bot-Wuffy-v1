package de.ngloader.core.lang;

public enum TranslationKeys {

	UNKNOWN(),

	COMMAND_TEST_NAME(),
	COMMAND_TEST_DESCRIPTION(),
	COMMAND_TEST_USAGE(),
	COMMAND_TEST_TRIGGER();

	public String format(String... args) {
		return "";
	}
}