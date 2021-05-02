package net.wuffy.bot.database.guild.sql;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.mongo.MongoMember;
import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionGuild;
import net.wuffy.core.database.sql.SQLStorage;

public class SQLExtensionGuild extends StorageProvider<SQLStorage> implements IExtensionGuild<WuffyGuild, MongoMember> {

	@Override
	public void registered(SQLStorage storage) {
	}

	@Override
	public WuffyGuild getGuild(Guild guild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MongoMember getMemeber(Guild guild, Member member) {
		// TODO Auto-generated method stub
		return null;
	}
}