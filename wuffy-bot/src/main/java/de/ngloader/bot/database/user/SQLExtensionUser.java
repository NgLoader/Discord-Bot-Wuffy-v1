package de.ngloader.bot.database.user;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.sql.SQLStorage;

public class SQLExtensionUser extends StorageProvider<SQLStorage> implements IExtensionUser<WuffyUser> {

	@Override
	public void registered(SQLStorage storage) {
	}

	@Override
	public WuffyUser getUser(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
}