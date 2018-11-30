package net.wuffy.core.database;

import java.util.Objects;

import net.wuffy.common.logger.Logger;
import net.wuffy.core.Core;
import net.wuffy.core.database.locale.LocaleStorage;
import net.wuffy.core.database.mongo.MongoStorage;
import net.wuffy.core.database.sql.SQLStorage;

public final class StorageService {

	private final Storage<?> storage;

	private final DatabaseConfig config;

	private final Core core;

	private boolean init = false;

	public StorageService(Core core, DatabaseConfig config) {
		this.core = core;
		this.config = config;

		this.storage = 
				this.config.mongo.enabled ?
					new MongoStorage(this.core, this.config.mongo) :
				this.config.sql.enabled ?
					new SQLStorage(this.core, this.config.sql) :
				this.config.locale.enabled ?
					new LocaleStorage(this.core, this.config.locale) :
				null;
	}

	public void enable() {
		if(this.config == null || this.init)
			return;
		Logger.debug("database", "Loading database modules");

		if(storage == null) {
			Logger.info("StorageService", "Storage not exist or is not enabled!");
			return;
		}

		Logger.debug("database", "Loading database modules finished");

		this.init = true;
	}

	public void disable() {
		if(!this.init)
			return;

		this.init = false;
	}

	public <T extends Storage<?>> T getStorage(Class<T> storageClass) {
		Objects.requireNonNull(storageClass);

		return storageClass.cast(this.storage);
	}

	public Storage<?> getStorage() {
		return this.storage;
	}
}