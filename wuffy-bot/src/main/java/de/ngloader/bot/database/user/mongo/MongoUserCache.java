package de.ngloader.bot.database.user.mongo;

import org.bson.types.ObjectId;

import de.ngloader.bot.database.BlockedInfo;

public class MongoUserCache {

	public MongoUserCache(Long userdId) {
		this._userId = Long.toString(userdId);
	}

	public ObjectId _id;

	public String _userId;

	public BlockedInfo blocked;

	public String userLocale;
}