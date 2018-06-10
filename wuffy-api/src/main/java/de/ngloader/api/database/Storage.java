package de.ngloader.api.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.ngloader.api.WuffyServer;
import de.ngloader.api.logger.ILogger;

public abstract class Storage<S extends Storage<S>> {

	private static final ILogger LOGGER = WuffyServer.getLogger();

	protected final Map<Class<? extends IStorageExtension>, IStorageProvider<S>> storageExtensions = new HashMap<Class<? extends IStorageExtension>, IStorageProvider<S>>();

	protected abstract void connect();

	public abstract boolean isOpen();

	protected abstract void disconnect();

	@SuppressWarnings("unchecked")
	public final <U extends IStorageExtension, T extends IStorageProvider<S>> boolean registerProvider(Class<U> extensionClass, T provider) {
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
		LOGGER.debug("database storage", "registerProvider '" + extensionClass.getSimpleName() + "'");
		return true;
	}
	
	public final <U extends IStorageExtension> boolean unregisterProvider(Class<U> extensionClass) {
		Objects.requireNonNull(extensionClass);

		IStorageProvider<?> provider = this.storageExtensions.remove(extensionClass);
		if(provider != null)
			provider.unregistered();

		if(this.storageExtensions.isEmpty())
			this.disconnect();

		LOGGER.debug("database storage", "unregisterProvider '" + extensionClass.getSimpleName() + "'");
		return provider != null;
	}

	public final <U extends IStorageExtension> U getProvider(Class<U> extensionClass) {
		Objects.requireNonNull(extensionClass);

		return extensionClass.cast(this.storageExtensions.get(extensionClass));
	}
}