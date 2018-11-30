package net.wuffy.bot.database.impl;

import net.dv8tion.jda.core.entities.Member;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.DBMember;
import net.wuffy.bot.database.DBUser;
import net.wuffy.core.Core;

public class MongoDBMember extends DBMember {

	public MongoDBMember(Core core, Member member, DBGuild guild, DBUser user) {
		super(core, member, guild, user);
	}
}