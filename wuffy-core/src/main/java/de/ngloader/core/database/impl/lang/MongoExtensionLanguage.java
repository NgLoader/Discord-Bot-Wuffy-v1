package de.ngloader.core.database.impl.lang;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.mongo.MongoStorage;

public class MongoExtensionLanguage implements IStorageProvider<MongoStorage>, IExtensionLang {

	@Override
	public void registered(MongoStorage storage) {
	}
}