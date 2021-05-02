package net.wuffy.bot.database.user.locale;

import net.dv8tion.jda.api.entities.User;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionUser;
import net.wuffy.core.database.locale.LocaleStorage;

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