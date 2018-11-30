package net.wuffy.bot.database.impl;

import com.mongodb.client.model.Filters;

import net.dv8tion.jda.core.entities.Guild;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.core.Core;
import net.wuffy.core.database.mongo.MongoStorage;

public class MongoDBGuild extends DBGuild {

	private MongoStorage storage;

	public MongoDBGuild(MongoStorage storage, Core core, Guild guild) {
		super(core, guild);

		this.storage = storage;
	}

	@Override
	public String getTest() {
		return Boolean.toString(this.storage.getCollection("user").find(Filters.eq("_userId", "216654877168762882")).first().getBoolean("alphaTester"));
	}
}