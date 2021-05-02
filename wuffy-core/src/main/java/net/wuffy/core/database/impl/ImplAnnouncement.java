package net.wuffy.core.database.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.wuffy.core.Core;

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