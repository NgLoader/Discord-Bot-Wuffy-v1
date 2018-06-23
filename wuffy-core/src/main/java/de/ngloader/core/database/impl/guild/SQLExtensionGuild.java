package de.ngloader.core.database.impl.guild;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.sql.SQLStorage;

public class SQLExtensionGuild implements IStorageProvider<SQLStorage>, IExtensionGuild {

	@Override
	public void registered(SQLStorage storage) {
	}
}