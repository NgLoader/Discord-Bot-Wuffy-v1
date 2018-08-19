package net.wuffy.core.database.locale;

import net.wuffy.core.Core;
import net.wuffy.core.database.Storage;

public final class LocaleStorage extends Storage<LocaleStorage> {

	protected final LocaleConfig config;

	public LocaleStorage(Core core, LocaleConfig config) {
		super(core);
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