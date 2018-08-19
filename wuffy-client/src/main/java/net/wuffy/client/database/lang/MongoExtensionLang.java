package net.wuffy.client.database.lang;

import java.util.List;

import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionLang;
import net.wuffy.core.database.mongo.MongoStorage;

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