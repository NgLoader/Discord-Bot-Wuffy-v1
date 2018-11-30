package net.wuffy.bot.database.impl;

import java.util.Locale;

import net.wuffy.core.Core;
import net.wuffy.core.database.mongo.MongoStorage;
import net.wuffy.core.lang.II18n;
import net.wuffy.core.lang.ILanguage;
import net.wuffy.core.lang.ITranslationKeys;

public class MongoDBI18n implements II18n {

	private MongoStorage storage;

	public MongoDBI18n(Core core, MongoStorage storage) {
		this.storage = storage;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String format(String key, String locale, String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String format(ITranslationKeys key, String locale, String... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(ITranslationKeys key, String locale, String translation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(ITranslationKeys key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ILanguage getLanguage(String locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILanguage getLanguage(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLanguage(ILanguage language) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLanguage(ILanguage language) {
		// TODO Auto-generated method stub
		
	}
}