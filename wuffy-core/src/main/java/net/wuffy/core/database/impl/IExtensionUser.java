package net.wuffy.core.database.impl;

import net.dv8tion.jda.core.entities.User;
import net.wuffy.core.database.IStorageExtension;

public interface IExtensionUser <USER extends ImplUser> extends IStorageExtension {

	public USER getUser(User user);
}