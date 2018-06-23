package de.ngloader.core.database.impl.user;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.sql.SQLStorage;

public class SQLExtensionUser implements IStorageProvider<SQLStorage>, IExtensionUser {

	@Override
	public void registered(SQLStorage storage) {
	}
}