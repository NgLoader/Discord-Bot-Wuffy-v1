package de.ngloader.bot.database.lang;

import java.util.List;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.mongo.MongoStorage;

public class MongoExtensionLang extends StorageProvider<MongoStorage> implements IExtensionLang<WuffyLang> {

	@Override
	public void registered(MongoStorage storage) {
	}

	@Override
	public WuffyLang getLang(String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WuffyLang> getLangs() {
		// TODO Auto-generated method stub
		return null;
	}
}