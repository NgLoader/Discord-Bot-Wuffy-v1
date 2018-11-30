package net.wuffy.bot.database;

import net.dv8tion.jda.core.entities.Guild;
import net.wuffy.bot.Wuffy;
import net.wuffy.core.Core;
import net.wuffy.core.event.impl.EventGuild;

public abstract class DBGuild extends EventGuild<DBUser, DBMember> {

	public abstract String getTest();

	public DBGuild(Core core, Guild guild) {
		super(core, guild);
	}

	@Override
	public Wuffy getCore() {
		return (Wuffy) this.core;
	}
}