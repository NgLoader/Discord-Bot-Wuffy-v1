package net.wuffy.bot.module;

import net.wuffy.bot.keys.TranslationKeys;

public enum EnumModuleType {

	COMMAND(TranslationKeys.MODULE_COMMAND),
	CUSTOMCOMMAND(TranslationKeys.MODULE_CUSTOMCOMMAND),
	AUTOPRUNE(TranslationKeys.MODULE_AUTOPRUNE),
	COUNTINGCHANNEL(TranslationKeys.MODULE_COUNTINGCHANNEL),
	POLL(TranslationKeys.MODULE_POLL),
	TICKETSYSTEM(TranslationKeys.MODULE_TICKETSYSTEM),
	LEVELSYSTEM(TranslationKeys.MODULE_LEVELSYSTEM),
	NOTIFICATION(TranslationKeys.MODULE_NOTIFICATION),
	MUSIC(TranslationKeys.MODULE_MUSIC),
	GROUP(TranslationKeys.MODULE_GROUP);

	private String translationKey;

	private EnumModuleType(String translationKey) {
		this.translationKey = translationKey;
	}

	public String getTranslationKey() {
		return this.translationKey;
	}
}