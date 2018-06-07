package de.ngloader.database.impl.user;

import de.ngloader.database.api.StorageProvider;
import de.ngloader.database.mongo.MongoStorage;

public class MongoExtensionUser implements StorageProvider<MongoStorage>, ExtensionUser {

	@Override
	public void registered(MongoStorage storage) {
	}
}