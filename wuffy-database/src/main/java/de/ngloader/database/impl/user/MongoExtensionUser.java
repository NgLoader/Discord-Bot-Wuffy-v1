package de.ngloader.database.impl.user;

import de.ngloader.api.database.IStorageProvider;
import de.ngloader.api.database.impl.user.IExtensionUser;
import de.ngloader.api.database.mongo.MongoStorage;

public class MongoExtensionUser implements IStorageProvider<MongoStorage>, IExtensionUser {

	@Override
	public void registered(MongoStorage storage) {
	}
}