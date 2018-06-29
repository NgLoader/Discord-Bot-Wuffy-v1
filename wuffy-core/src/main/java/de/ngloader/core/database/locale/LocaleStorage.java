package de.ngloader.core.database.locale;

import de.ngloader.core.database.Storage;

public final class LocaleStorage extends Storage<LocaleStorage> {

	protected final LocaleConfig config;

	public LocaleStorage(LocaleConfig config) {
		this.config = config;
	}

	@Override
	protected void connect() {
	}

	@Override
	public boolean isOpen() {
		return true;
	}

	@Override
	protected void disconnect() {
	}
}