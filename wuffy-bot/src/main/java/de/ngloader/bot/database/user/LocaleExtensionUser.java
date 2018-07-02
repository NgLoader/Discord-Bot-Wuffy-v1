package de.ngloader.bot.database.user;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.locale.LocaleStorage;

public class LocaleExtensionUser extends StorageProvider<LocaleStorage> implements IExtensionUser<WuffyUser> {

	@Override
	public void registered(LocaleStorage storage) {
	}

	@Override
	public WuffyUser getUser(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
}