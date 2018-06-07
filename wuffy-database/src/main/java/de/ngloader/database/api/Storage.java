package de.ngloader.database.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Storage<S extends Storage<S>> {

	protected final Map<Class<? extends StorageExtension>, StorageProvider<S>> storageExtensions = new HashMap<Class<? extends StorageExtension>, StorageProvider<S>>();

	protected abstract void connect();

	public abstract boolean isOpen();

	protected abstract void disconnect();

	@SuppressWarnings("unchecked")
	public final <U extends StorageExtension, T extends StorageProvider<S>> boolean registerProvider(Class<U> extensionClass, T provider) {
		Objects.requireNonNull(extensionClass);
		Objects.requireNonNull(provider);

		if(!extensionClass.isInstance(provider))
			throw new ClassCastException(String.format("can't cast %s to %s", provider.getClass(), extensionClass));

		if(this.storageExtensions.containsKey(extensionClass))
			return false;

		if(this.storageExtensions.isEmpty())
			this.connect();

		this.storageExtensions.put(extensionClass, provider);
		provider.registered((S) this);
		return true;
	}
	
	public final <U extends StorageExtension> boolean unregisterProvider(Class<U> extensionClass) {
		Objects.requireNonNull(extensionClass);

		StorageProvider<?> provider = this.storageExtensions.remove(extensionClass);
		if(provider != null)
			provider.unregistered();

		if(this.storageExtensions.isEmpty())
			this.disconnect();

		return provider != null;
	}

	public final <U extends StorageExtension> U getProvider(Class<U> extensionClass) {
		Objects.requireNonNull(extensionClass);

		return extensionClass.cast(this.storageExtensions.get(extensionClass));
	}
}
