package net.wuffy.core.database;

import net.wuffy.core.Core;

public abstract class StorageProvider<S extends Storage<S>> implements IStorageExtension {

	protected abstract void registered(S storage);

	protected void unregistered() {};

	protected Core core;

	public void setCore(Core core) {
		if(this.core != null)
			throw new NullPointerException("Core already set");
		this.core = core;
	}

	public Core getCore() {
		return core;
	}
}