package net.wuffy.bot.database.user.mongo;

import org.bson.types.ObjectId;

import net.wuffy.bot.database.BlockedInfo;

public class MongoUserCache {

	public MongoUserCache(Long userdId) {
		this._userId = Long.toString(userdId);
	}

	public ObjectId _id;

	public String _userId;

	public BlockedInfo blocked = null;

	public String userLocale = null;

	public Boolean alphaTester = false;
}