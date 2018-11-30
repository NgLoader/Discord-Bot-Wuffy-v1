package net.wuffy.bot.database;

import net.dv8tion.jda.core.entities.Member;
import net.wuffy.bot.Wuffy;
import net.wuffy.core.Core;
import net.wuffy.core.event.impl.EventMember;

public abstract class DBMember extends EventMember<DBGuild, DBUser> {

	protected final DBGuild guild;
	protected final DBUser user;

	public DBMember(Core core, Member member, DBGuild guild, DBUser user) {
		super(core, member);

		this.guild = guild;
		this.user = user;
	}

	@Override
	public Wuffy getCore() {
		return (Wuffy) this.core;
	}

	@Override
	public DBGuild getGuild() {
		return this.guild;
	}

	@Override
	public DBUser getUser() {
		return this.user;
	}
}