package de.ngloader.database.impl.lang;

import de.ngloader.api.database.IStorageProvider;
import de.ngloader.api.database.impl.lang.IExtensionLanguage;
import de.ngloader.api.database.sql.SQLStorage;

public class SQLExtensionLanguage implements IStorageProvider<SQLStorage>, IExtensionLanguage {

	@Override
	public void registered(SQLStorage storage) {
	}
}