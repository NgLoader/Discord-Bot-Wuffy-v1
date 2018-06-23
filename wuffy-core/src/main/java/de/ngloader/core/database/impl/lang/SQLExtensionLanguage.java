package de.ngloader.core.database.impl.lang;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.sql.SQLStorage;

public class SQLExtensionLanguage implements IStorageProvider<SQLStorage>, IExtensionLang {

	@Override
	public void registered(SQLStorage storage) {
	}
}