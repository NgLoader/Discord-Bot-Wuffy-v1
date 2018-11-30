package net.wuffy.bot.database.impl;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.database.DBUser;
import net.wuffy.core.Core;
import net.wuffy.core.database.mongo.MongoStorage;

public class MongoDBUser extends DBUser {

	private MongoStorage storage;

	public MongoDBUser(MongoStorage storage, Core core, User user) {
		super(core, user);

		this.storage = storage;
	}
}