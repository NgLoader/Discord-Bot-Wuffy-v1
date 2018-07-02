package de.ngloader.client.database.user;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.mongo.MongoStorage;

public class MongoExtensionUser implements StorageProvider<MongoStorage>, IExtensionUser<WuffyUser> {

	@Override
	public void registered(MongoStorage storage) {
	}

	@Override
	public WuffyUser getUser(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
}