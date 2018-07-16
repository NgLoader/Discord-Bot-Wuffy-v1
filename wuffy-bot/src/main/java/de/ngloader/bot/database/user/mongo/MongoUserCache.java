package de.ngloader.bot.database.user.mongo;

import org.bson.types.ObjectId;

import de.ngloader.bot.database.BlockedInfo;

public class MongoUserCache {

	public MongoUserCache(Long userdId) {
		this._userdId = Long.toString(userdId);
	}

	public ObjectId _id;

	public String _userdId;

	public BlockedInfo blocked;

	public String userLocale;
}