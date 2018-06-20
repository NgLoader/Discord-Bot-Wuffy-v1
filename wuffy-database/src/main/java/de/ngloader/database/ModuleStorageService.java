package de.ngloader.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.ngloader.api.WuffyServer;
import de.ngloader.api.database.IStorageExtension;
import de.ngloader.api.database.IStorageService;
import de.ngloader.api.database.Storage;
import de.ngloader.api.database.mongo.MongoStorage;
import de.ngloader.api.database.sql.SQLStorage;
import de.ngloader.api.logger.ILogger;

public final class ModuleStorageService implements IStorageService {

	private static final ILogger LOGGER = WuffyServer.getLogger();

	private final DatabaseConfig config;

	private final Map<Class<? extends Storage<?>>, Storage<?>> storages = new HashMap<Class<? extends Storage<?>>, Storage<?>>();
	private final Map<String, Storage<?>> storageNames = new HashMap<String, Storage<?>>();

	private final Map<String, Class<? extends IStorageExtension>> storageExtensions = new HashMap<String, Class<? extends IStorageExtension>>();
	private final Map<Class<? extends IStorageExtension>, IStorageExtension> defaultProvider = new HashMap<Class<? extends IStorageExtension>, IStorageExtension>();

	private boolean init;

	public ModuleStorageService(Path path) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		if(Files.notExists(path)) {
			try {
				Files.createDirectories(path.getParent());
				Files.copy(ModuleStorageService.class.getResourceAsStream("/database.yml"), path);
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.fatal("Failed to create database.yml", e);
			}
		}

		try (BufferedReader reader = Files.newBufferedReader(path)) {
			this.config = gson.fromJson(reader, DatabaseConfig.class);
		} catch (IOException e) {
			throw new Error(e);
		}

		if(this.config.mongo.enabled)
			this.registerStorage(MongoStorage.class, "mongo", new MongoStorage(this.config.mongo));

		if(this.config.sql.enabled)
			this.registerStorage(SQLStorage.class, "sql",  new SQLStorage(this.config.sql));
	}

	public void enable() {
		if(this.config == null || this.init)
			return;
		LOGGER.debug("database", "Loading database modules");

		for(Entry<String, String> extension : this.config.extensions.entrySet()) {
			Class<? extends IStorageExtension> extensionClass = this.storageExtensions.get(extension.getKey());
			if(extensionClass == null) {
				LOGGER.warn("database", String.format("Unknown extension: %s", extension.getKey()));
				continue;
			}

			Storage<?> storage = storageNames.get(extension.getValue());
			if(storage == null) {
				LOGGER.warn("database", String.format("Unknown storage: %s", extension.getValue()));
				continue;
			}

			IStorageExtension provider = storage.getProvider(extensionClass);
			if(provider == null) {
				LOGGER.warn("database", String.format("Unknown provider: %s/%s", extension.getValue(), extension.getKey()));
				continue;
			}

			defaultProvider.put(extensionClass, provider);
			LOGGER.debug("database", "Enabled database module " + extensionClass.getSimpleName());
		}

		LOGGER.debug("database", "Loading database modules finished");

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

	@Override
	public <T extends Storage<T>> boolean registerStorage(Class<T> storageClass, String name, T storage) {
		Objects.requireNonNull(storageClass);
		Objects.requireNonNull(name);
		Objects.requireNonNull(storage);

		if(!storages.containsKey(storageClass) && !storageNames.containsKey(name)) {
			storages.put(storageClass, storage);
			storageNames.put(name, storage);
			return true;
		}

		return false;
	}

	@Override
	public <T extends Storage<T>> boolean unregisterStorage(Class<T> storageClass) {
		Objects.requireNonNull(storageClass);

		return storages.remove(storageClass) != null;
	}

	@Override
	public <T extends Storage<T>> T getStorage(Class<T> storageClass) {
		Objects.requireNonNull(storageClass);

		return storageClass.cast(storages.get(storageClass));
	}

	@Override
	public <T extends IStorageExtension> boolean registerExtension(String name, Class<T> extenionClass) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(extenionClass);

		if(this.storageExtensions.containsKey(name))
			return false;

		this.storageExtensions.put(name, extenionClass);
		LOGGER.debug("database storage", "registerExtension '" + extenionClass.getSimpleName() + "'");
		return true;
	}

	@Override
	public <T extends IStorageExtension> boolean unregisterExtension(String name) {
		Objects.requireNonNull(name);

		if(this.storageExtensions.containsKey(name)) {
			this.defaultProvider.remove(this.storageExtensions.remove(name));
			return true;
		}

		return false;
	}

	@Override
	public <T extends IStorageExtension> T getExtension(Class<T> extenionClass) {
		Objects.requireNonNull(extenionClass);

		return extenionClass.cast(this.defaultProvider.get(extenionClass));
	}
}