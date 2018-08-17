package de.ngloader.core.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.ngloader.common.logger.Logger;
import de.ngloader.core.Core;

public abstract class Storage<S extends Storage<S>> {

	protected final Map<Class<? extends IStorageExtension>, StorageProvider<S>> storageExtensions = new HashMap<Class<? extends IStorageExtension>, StorageProvider<S>>();

	protected abstract void connect();

	public abstract boolean isOpen();

	protected abstract void disconnect();

	protected Core core;

	public Storage(Core core) {
		this.core = core;
	}

	@SuppressWarnings("unchecked")
	public final <U extends IStorageExtension, T extends StorageProvider<S>> boolean registerProvider(Class<U> extensionClass, T provider) {
		Objects.requireNonNull(extensionClass);
		Objects.requireNonNull(provider);

		if(!extensionClass.isInstance(provider))
			throw new ClassCastException(String.format("can't cast %s to %s", provider.getClass(), extensionClass));

		if(this.storageExtensions.containsKey(extensionClass))
			return false;

		if(this.storageExtensions.isEmpty())
			this.connect();

		provider.setCore(this.core);

		this.storageExtensions.put(extensionClass, provider);
		provider.registered((S) this);
		Logger.debug("database storage", "registerProvider '" + extensionClass.getSimpleName() + "'");
		return true;
	}
	
	public final <U extends IStorageExtension> boolean unregisterProvider(Class<U> extensionClass) {
		Objects.requireNonNull(extensionClass);

		StorageProvider<?> provider = this.storageExtensions.remove(extensionClass);
		if(provider != null)
			provider.unregistered();

		if(this.storageExtensions.isEmpty())
			this.disconnect();

		Logger.debug("database storage", "unregisterProvider '" + extensionClass.getSimpleName() + "'");
		return provider != null;
	}

	public final <U extends IStorageExtension> U getProvider(Class<U> extensionClass) {
		Objects.requireNonNull(extensionClass);

		return extensionClass.cast(this.storageExtensions.get(extensionClass));
	}
}