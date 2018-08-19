package net.wuffy.bot.database.user.sql;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionUser;
import net.wuffy.core.database.sql.SQLStorage;

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