package net.wuffy.bot.database.lang;

import java.util.List;

import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionLang;
import net.wuffy.core.database.sql.SQLStorage;

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