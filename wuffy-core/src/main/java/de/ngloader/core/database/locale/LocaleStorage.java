package de.ngloader.core.database.locale;

import de.ngloader.core.database.Storage;

public class LocaleStorage extends Storage<LocaleStorage> {

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