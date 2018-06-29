package de.ngloader.client.database.lang;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.sql.SQLStorage;

public class SQLExtensionLang implements IStorageProvider<SQLStorage>, IExtensionLang {

	@Override
	public void registered(SQLStorage storage) {
	}
}