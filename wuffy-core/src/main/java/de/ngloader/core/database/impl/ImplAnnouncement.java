package de.ngloader.core.database.impl;

import de.ngloader.core.Core;
import net.dv8tion.jda.core.entities.Guild;

public class ImplAnnouncement {

	protected final Core core;
	protected final Guild guild;

	public ImplAnnouncement(Core core, Guild guild) {
		this.core = core;
		this.guild = guild;
	}

	public Core getCore() {
		return core;
	}

	public Guild getGuild() {
		return guild;
	}
}