package de.ngloader.database.impl.lang;

import de.ngloader.api.database.IStorageProvider;
import de.ngloader.api.database.impl.lang.IExtensionLanguage;
import de.ngloader.api.database.mongo.MongoStorage;

public class MongoExtensionLanguage implements IStorageProvider<MongoStorage>, IExtensionLanguage {

	@Override
	public void registered(MongoStorage storage) {
	}
}