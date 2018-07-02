package de.ngloader.client.database.user;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.sql.SQLStorage;

public class SQLExtensionUser implements StorageProvider<SQLStorage>, IExtensionUser<WuffyUser> {

	@Override
	public void registered(SQLStorage storage) {
	}

	@Override
	public WuffyUser getUser(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
}