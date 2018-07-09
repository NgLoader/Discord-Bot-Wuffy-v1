package de.ngloader.bot.database.user;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplUser;
import net.dv8tion.jda.core.entities.User;

public class WuffyUser extends ImplUser {

	protected final IWuffyExtensionUser extension;

	public WuffyUser(Core core, User user, IWuffyExtensionUser extension) {
		super(core, user);

		this.extension = extension;
	}

	public String getUserLocale() {
		return this.extension.getLocale(this.user);
	}
}