package de.ngloader.database.impl.guild;

import de.ngloader.database.api.StorageProvider;
import de.ngloader.database.mongo.MongoStorage;

public class MongoExtensionGuild implements StorageProvider<MongoStorage>, ExtensionGuild {

	@Override
	public void registered(MongoStorage storage) {
	}
}