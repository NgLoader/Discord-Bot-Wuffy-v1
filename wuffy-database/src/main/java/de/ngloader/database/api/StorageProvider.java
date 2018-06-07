package de.ngloader.database.api;

public interface StorageProvider<S extends Storage<S>> extends StorageExtension {

	void registered(S storage);

	default void unregistered() {};
}