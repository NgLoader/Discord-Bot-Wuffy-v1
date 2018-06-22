package de.ngloader.core.database;

public interface IStorageProvider<S extends Storage<S>> extends IStorageExtension {

	void registered(S storage);

	default void unregistered() {};
}