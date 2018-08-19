package net.wuffy.client.database.lang;

import java.util.List;

import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionLang;
import net.wuffy.core.database.locale.LocaleStorage;

public class LocaleExtensionLang extends StorageProvider<LocaleStorage> implements IExtensionLang<WuffyLang> {

	@Override
	public void registered(LocaleStorage storage) {
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