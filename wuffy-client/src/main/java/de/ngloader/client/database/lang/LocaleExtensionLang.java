package de.ngloader.client.database.lang;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.locale.LocaleStorage;

public class LocaleExtensionLang implements IStorageProvider<LocaleStorage>, IExtensionLang {

	@Override
	public void registered(LocaleStorage storage) {
	}
}