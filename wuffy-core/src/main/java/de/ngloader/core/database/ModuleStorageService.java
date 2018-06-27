package de.ngloader.core.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import de.ngloader.core.database.mongo.MongoStorage;
import de.ngloader.core.database.sql.SQLStorage;
import de.ngloader.core.logger.Logger;

public final class ModuleStorageService {

	private final Map<Class<? extends Storage<?>>, Storage<?>> storages = new HashMap<Class<? extends Storage<?>>, Storage<?>>();
	private final Map<String, Storage<?>> storageNames = new HashMap<String, Storage<?>>();

	private final Map<String, Class<? extends IStorageExtension>> storageExtensions = new HashMap<String, Class<? extends IStorageExtension>>();
	private final Map<Class<? extends IStorageExtension>, IStorageExtension> defaultProvider = new HashMap<Class<? extends IStorageExtension>, IStorageExtension>();

	private final DatabaseConfig config;

	private boolean init = false;

	public ModuleStorageService(DatabaseConfig config) {
		this.config = config;

		if(this.config.mongo.enabled)
			this.registerStorage(MongoStorage.class, "mongo", new MongoStorage(this.config.mongo));

		if(this.config.sql.enabled)
			this.registerStorage(SQLStorage.class, "sql",  new SQLStorage(this.config.sql));
	}

	public void enable() {
		if(this.config == null || this.init)
			return;
		Logger.debug("database", "Loading database modules");

		for(Entry<String, String> extension : this.config.extensions.entrySet()) {
			Class<? extends IStorageExtension> extensionClass = this.storageExtensions.get(extension.getKey());
			if(extensionClass == null) {
				Logger.warn("database", String.format("Unknown extension: %s", extension.getKey()));
				continue;
			}

			Storage<?> storage = this.storageNames.get(extension.getValue());
			if(storage == null) {
				Logger.warn("database", String.format("Unknown storage: %s", extension.getValue()));
				continue;
			}

			IStorageExtension provider = storage.getProvider(extensionClass);
			if(provider == null) {
				Logger.warn("database", String.format("Unknown provider: %s/%s", extension.getValue(), extension.getKey()));
				continue;
			}

			defaultProvider.put(extensionClass, provider);
			Logger.debug("database", "Enabled database module " + extensionClass.getSimpleName());
		}

		Logger.debug("database", "Loading database modules finished");

		this.init = true;
	}

	public void disable() {
		if(!this.init)
			return;

		this.storages.forEach((storageClass, storage) -> {
			this.storageExtensions.forEach((name, extensionClass) -> {
				storage.unregisterProvider(extensionClass);
			});
		});

		this.init = false;
	}

	public <T extends Storage<T>> boolean registerStorage(Class<T> storageClass, String name, T storage) {
		Objects.requireNonNull(storageClass);
		Objects.requireNonNull(name);
		Objects.requireNonNull(storage);

		if(!this.storages.containsKey(storageClass) && !this.storageNames.containsKey(name)) {
			this.storages.put(storageClass, storage);
			this.storageNames.put(name, storage);
			return true;
		}

		return false;
	}

	public <T extends Storage<T>> boolean unregisterStorage(Class<T> storageClass) {
		Objects.requireNonNull(storageClass);

		return this.storages.remove(storageClass) != null;
	}

	public <T extends Storage<T>> T getStorage(Class<T> storageClass) {
		Objects.requireNonNull(storageClass);

		return storageClass.cast(this.storages.get(storageClass));
	}

	public <T extends IStorageExtension> boolean registerExtension(String name, Class<T> extenionClass) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(extenionClass);

		if(this.storageExtensions.containsKey(name))
			return false;

		this.storageExtensions.put(name, extenionClass);
		Logger.debug("database storage", "registerExtension '" + extenionClass.getSimpleName() + "'");
		return true;
	}

	public <T extends IStorageExtension> boolean unregisterExtension(String name) {
		Objects.requireNonNull(name);

		if(this.storageExtensions.containsKey(name)) {
			this.defaultProvider.remove(this.storageExtensions.remove(name));
			return true;
		}

		return false;
	}

	public <T extends IStorageExtension> T getExtension(Class<T> extenionClass) {
		Objects.requireNonNull(extenionClass);

		return extenionClass.cast(this.defaultProvider.get(extenionClass));
	}
}