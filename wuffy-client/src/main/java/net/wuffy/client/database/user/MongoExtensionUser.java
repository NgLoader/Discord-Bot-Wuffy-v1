package net.wuffy.client.database.user;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionUser;
import net.wuffy.core.database.mongo.MongoStorage;

public class MongoExtensionUser extends StorageProvider<MongoStorage> implements IExtensionUser<WuffyUser> {

	@Override
	public void registered(MongoStorage storage) {
	}

	@Override
	public WuffyUser getUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}