package de.ngloader.core.database;

public interface IStorageService {

	<T extends Storage<T>> boolean registerStorage(Class<T> storageClass, String name, T storage);
	<T extends Storage<T>> boolean unregisterStorage(Class<T> storageClass);
	<T extends Storage<T>> T getStorage(Class<T> storageClass);

	<T extends IStorageExtension> boolean registerExtension(String name, Class<T> providerClass);
	<T extends IStorageExtension> boolean unregisterExtension(String name);
	<T extends IStorageExtension> T getExtension(Class<T> providerClass);
}