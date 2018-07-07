package de.ngloader.core.database.impl;

import de.ngloader.core.database.IStorageExtension;
import net.dv8tion.jda.core.entities.User;

public interface IExtensionUser <USER extends ImplUser> extends IStorageExtension {

	public USER getUser(User user);
}