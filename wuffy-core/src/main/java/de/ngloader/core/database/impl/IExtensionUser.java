package de.ngloader.core.database.impl;

import de.ngloader.core.database.IStorageExtension;

public interface IExtensionUser <USER extends ImplUser> extends IStorageExtension {

	public USER getUser(Long userId);
}