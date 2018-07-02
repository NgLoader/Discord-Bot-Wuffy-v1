package de.ngloader.client.database.lang;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionLang;
import de.ngloader.core.database.locale.LocaleStorage;

public class LocaleExtensionLang implements StorageProvider<LocaleStorage>, IExtensionLang {

	@Override
	public void registered(LocaleStorage storage) {
	}
}