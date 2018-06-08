package de.ngloader.database.impl.user;

import de.ngloader.database.api.StorageProvider;
import de.ngloader.database.sql.SQLStorage;

public class SQLExtensionUser implements StorageProvider<SQLStorage>, ExtensionUser {

	@Override
	public void registered(SQLStorage storage) {
	}
}