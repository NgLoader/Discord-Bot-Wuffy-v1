package net.wuffy.bot.database.mongo;

import java.util.HashMap;
import java.util.Map;

import net.wuffy.core.database.mongo.MongoBulkWriteSystem;

public class MongoBulkWriteSystemAdapter {

	private final Map<String, MongoBulkWriteSystem> mongoBulkWriteSystems = new HashMap<>();

	public MongoBulkWriteSystem get(String name) {
		if(!this.mongoBulkWriteSystems.containsKey(name))
			this.mongoBulkWriteSystems.put(name, new MongoBulkWriteSystem(name));

		return this.mongoBulkWriteSystems.get(name);
	}

	public void remove(String name) {
		MongoBulkWriteSystem mongoBulkWriteSystem = this.mongoBulkWriteSystems.remove(name);

		if(mongoBulkWriteSystem != null)
			mongoBulkWriteSystem.disableBulkWrite();
	}
}