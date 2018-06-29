package de.ngloader.bot.database.user;

import de.ngloader.core.database.IStorageProvider;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.locale.LocaleStorage;

public class LocaleExtensionUser implements IStorageProvider<LocaleStorage>, IExtensionUser<WuffyUser> {

	@Override
	public void registered(LocaleStorage storage) {
	}

	@Override
	public WuffyUser getUser(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
}