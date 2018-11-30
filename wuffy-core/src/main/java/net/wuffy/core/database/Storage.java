package net.wuffy.core.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.wuffy.common.logger.Logger;
import net.wuffy.core.Core;

public abstract class Storage<S extends Storage<S>> {

	protected final Map<Class<? extends IExtension>, StorageProvider<S>> storageExtensions = new HashMap<Class<? extends IExtension>, StorageProvider<S>>();

	protected abstract void connect();

	public abstract boolean isOpen();

	protected abstract void disconnect();

	protected Core core;

	public Storage(Core core) {
		this.core = core;
	}

	@SuppressWarnings("unchecked")
	public final <T extends StorageProvider<S>> boolean registerProvider(Class<? extends IExtension> extensionClass, T provider) {
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
		System.out.println("1" + extensionClass.getSimpleName() + " - " + extensionClass);
		provider.registered((S) this);
		Logger.debug("Database storage", "registerProvider '" + extensionClass.getSimpleName() + "'");
		return true;
	}

	public final boolean unregisterProvider(Class<? extends IExtension> extensionClass) {
		Objects.requireNonNull(extensionClass);

		StorageProvider<?> provider = this.storageExtensions.remove(extensionClass);
		if(provider != null)
			provider.unregistered();

		if(this.storageExtensions.isEmpty())
			this.disconnect();

		Logger.debug("Database storage", "unregisterProvider '" + extensionClass.getSimpleName() + "'");
		return provider != null;
	}

	public final <T extends IExtension> T getProvider(Class<T> extensionClass) {
		Objects.requireNonNull(extensionClass);
		System.out.println("2" + extensionClass.getSimpleName() + " - " + extensionClass);

		return extensionClass.cast(this.storageExtensions.get(extensionClass));
	}
}