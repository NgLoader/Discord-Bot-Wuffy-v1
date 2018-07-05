package de.ngloader.bot.database.lang;

import java.util.List;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.locale.LocaleStorage;

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