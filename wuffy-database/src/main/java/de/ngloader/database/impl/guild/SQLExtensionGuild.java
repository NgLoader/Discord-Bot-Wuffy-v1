package de.ngloader.database.impl.guild;

import de.ngloader.database.api.StorageProvider;
import de.ngloader.database.sql.SQLStorage;

public class SQLExtensionGuild implements StorageProvider<SQLStorage>, ExtensionGuild {

	@Override
	public void registered(SQLStorage storage) {
	}
}