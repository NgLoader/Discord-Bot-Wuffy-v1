package de.ngloader.client.database.user;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.sql.SQLStorage;
import net.dv8tion.jda.core.entities.User;

public class SQLExtensionUser extends StorageProvider<SQLStorage> implements IExtensionUser<WuffyUser> {

	@Override
	public void registered(SQLStorage storage) {
	}

	@Override
	public WuffyUser getUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}
}