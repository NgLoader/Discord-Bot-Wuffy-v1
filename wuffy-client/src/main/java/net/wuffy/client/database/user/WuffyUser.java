package net.wuffy.client.database.user;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.core.CoreOLD;
import net.wuffy.core.database.impl.ImplUser;

public class WuffyUser extends ImplUser {

	public WuffyUser(CoreOLD core, User user) {
		super(core, user);
	}
}