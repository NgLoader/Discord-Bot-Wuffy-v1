package de.ngloader.database.api;

public interface StorageService {

	<T extends Storage<T>> boolean registerStorage(Class<T> storageClass, String name, T storage);
	<T extends Storage<T>> boolean unregisterStorage(Class<T> storageClass);
	<T extends Storage<T>> T getStorage(Class<T> storageClass);

	<T extends StorageExtension> boolean registerExtension(String name, Class<T> providerClass);
	<T extends StorageExtension> boolean unregisterExtension(String name);
	<T extends StorageExtension> T getExtension(Class<T> providerClass);
}