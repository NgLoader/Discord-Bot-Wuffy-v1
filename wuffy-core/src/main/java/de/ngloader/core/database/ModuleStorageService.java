package de.ngloader.core.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import de.ngloader.core.config.ConfigService;
import de.ngloader.core.database.mongo.MongoStorage;
import de.ngloader.core.database.sql.SQLStorage;
import de.ngloader.core.logger.Logger;

public final class ModuleStorageService {

	private static final DatabaseConfig config = ConfigService.getConfig(DatabaseConfig.class);

	private static final Map<Class<? extends Storage<?>>, Storage<?>> storages = new HashMap<Class<? extends Storage<?>>, Storage<?>>();
	private static final Map<String, Storage<?>> storageNames = new HashMap<String, Storage<?>>();

	private static final Map<String, Class<? extends IStorageExtension>> storageExtensions = new HashMap<String, Class<? extends IStorageExtension>>();
	private static final Map<Class<? extends IStorageExtension>, IStorageExtension> defaultProvider = new HashMap<Class<? extends IStorageExtension>, IStorageExtension>();

	private static boolean init;

	static {
		if(ModuleStorageService.config.mongo.enabled)
			ModuleStorageService.registerStorage(MongoStorage.class, "mongo", new MongoStorage(ModuleStorageService.config.mongo));

		if(ModuleStorageService.config.sql.enabled)
			ModuleStorageService.registerStorage(SQLStorage.class, "sql",  new SQLStorage(ModuleStorageService.config.sql));
	}

	public static void enable() {
		if(ModuleStorageService.config == null || ModuleStorageService.init)
			return;
		Logger.debug("database", "Loading database modules");

		for(Entry<String, String> extension : ModuleStorageService.config.extensions.entrySet()) {
			Class<? extends IStorageExtension> extensionClass = ModuleStorageService.storageExtensions.get(extension.getKey());
			if(extensionClass == null) {
				Logger.warn("database", String.format("Unknown extension: %s", extension.getKey()));
				continue;
			}

			Storage<?> storage = storageNames.get(extension.getValue());
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

		ModuleStorageService.init = true;
	}

	public static void disable() {
		if(!ModuleStorageService.init)
			return;

		ModuleStorageService.storages.forEach((storageClass, storage) -> {
			ModuleStorageService.storageExtensions.forEach((name, extensionClass) -> {
				storage.unregisterProvider(extensionClass);
			});
		});

		ModuleStorageService.init = false;
	}

	public static <T extends Storage<T>> boolean registerStorage(Class<T> storageClass, String name, T storage) {
		Objects.requireNonNull(storageClass);
		Objects.requireNonNull(name);
		Objects.requireNonNull(storage);

		if(!ModuleStorageService.storages.containsKey(storageClass) && !ModuleStorageService.storageNames.containsKey(name)) {
			ModuleStorageService.storages.put(storageClass, storage);
			ModuleStorageService.storageNames.put(name, storage);
			return true;
		}

		return false;
	}

	public static <T extends Storage<T>> boolean unregisterStorage(Class<T> storageClass) {
		Objects.requireNonNull(storageClass);

		return ModuleStorageService.storages.remove(storageClass) != null;
	}

	public static <T extends Storage<T>> T getStorage(Class<T> storageClass) {
		Objects.requireNonNull(storageClass);

		return storageClass.cast(ModuleStorageService.storages.get(storageClass));
	}

	public static <T extends IStorageExtension> boolean registerExtension(String name, Class<T> extenionClass) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(extenionClass);

		if(ModuleStorageService.storageExtensions.containsKey(name))
			return false;

		ModuleStorageService.storageExtensions.put(name, extenionClass);
		Logger.debug("database storage", "registerExtension '" + extenionClass.getSimpleName() + "'");
		return true;
	}

	public static <T extends IStorageExtension> boolean unregisterExtension(String name) {
		Objects.requireNonNull(name);

		if(ModuleStorageService.storageExtensions.containsKey(name)) {
			ModuleStorageService.defaultProvider.remove(ModuleStorageService.storageExtensions.remove(name));
			return true;
		}

		return false;
	}

	public static <T extends IStorageExtension> T getExtension(Class<T> extenionClass) {
		Objects.requireNonNull(extenionClass);

		return extenionClass.cast(ModuleStorageService.defaultProvider.get(extenionClass));
	}
}