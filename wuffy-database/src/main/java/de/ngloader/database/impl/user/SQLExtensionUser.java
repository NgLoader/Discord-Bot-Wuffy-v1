package de.ngloader.database.impl.user;

import de.ngloader.api.database.IStorageProvider;
import de.ngloader.api.database.impl.user.IExtensionUser;
import de.ngloader.api.database.sql.SQLStorage;

public class SQLExtensionUser implements IStorageProvider<SQLStorage>, IExtensionUser {

	@Override
	public void registered(SQLStorage storage) {
	}
}