package de.ngloader.core.database.impl.user;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.mongo.MongoStorage;

public class MongoExtensionUser implements IStorageProvider<MongoStorage>, IExtensionUser {

	@Override
	public void registered(MongoStorage storage) {
	}
}