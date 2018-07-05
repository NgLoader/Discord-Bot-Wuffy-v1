package de.ngloader.client.database.lang;

import java.util.List;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.sql.SQLStorage;

public class SQLExtensionLang extends StorageProvider<SQLStorage> implements IExtensionLang<WuffyLang> {

	@Override
	public void registered(SQLStorage storage) {
	}

	@Override
	public WuffyLang getLang(String lang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WuffyLang> getLangs() {
		// TODO Auto-generated method stub
		return null;
	}
}