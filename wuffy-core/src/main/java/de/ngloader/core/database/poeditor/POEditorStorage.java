package de.ngloader.core.database.poeditor;

import de.ngloader.core.Core;
import de.ngloader.core.database.Storage;

public final class POEditorStorage extends Storage<POEditorStorage> {

	protected final POEditorConfig config;

	public POEditorStorage(Core core, POEditorConfig config) {
		super(core);
		this.config = config;
	}

	@Override
	protected void connect() { }

	@Override
	protected void disconnect() { }

	@Override
	public boolean isOpen() {
		return true;
	}

	public POEditorConfig getConfig() {
		return config;
	}
}
