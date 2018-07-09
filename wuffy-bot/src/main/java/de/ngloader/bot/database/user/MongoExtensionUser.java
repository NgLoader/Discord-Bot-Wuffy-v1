package de.ngloader.bot.database.user;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.mongo.MongoStorage;
import net.dv8tion.jda.core.entities.User;

public class MongoExtensionUser extends StorageProvider<MongoStorage> implements IExtensionUser<WuffyUser>, IWuffyExtensionUser {

	@Override
	public void registered(MongoStorage storage) {
	}

	@Override
	public WuffyUser getUser(User user) {
		return new WuffyUser(this.core, user);
	}

	@Override
	public String getLocale(User user) {
		return null;
	}
}