package de.ngloader.client.database.lang;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.mongo.MongoStorage;

public class MongoExtensionLang implements StorageProvider<MongoStorage>, IExtensionLang {

	@Override
	public void registered(MongoStorage storage) {
	}
}