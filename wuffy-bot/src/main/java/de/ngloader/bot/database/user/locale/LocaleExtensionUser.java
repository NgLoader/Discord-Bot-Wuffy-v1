package de.ngloader.bot.database.user.locale;

import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.locale.LocaleStorage;
import net.dv8tion.jda.core.entities.User;

public class LocaleExtensionUser extends StorageProvider<LocaleStorage> implements IExtensionUser<WuffyUser> {

	@Override
	public void registered(LocaleStorage storage) {
	}

	@Override
	public WuffyUser getUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}