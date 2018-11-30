package net.wuffy.bot.database;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.Wuffy;
import net.wuffy.core.Core;
import net.wuffy.core.event.impl.EventUser;

public abstract class DBUser extends EventUser {

	public DBUser(Core core, User user) {
		super(core, user);
	}

	@Override
	public Wuffy getCore() {
		return (Wuffy) this.core;
	}
}