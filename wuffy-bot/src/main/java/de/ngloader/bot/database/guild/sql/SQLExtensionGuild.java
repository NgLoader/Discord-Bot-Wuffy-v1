package de.ngloader.bot.database.guild.sql;

import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.mongo.MongoMember;
import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.sql.SQLStorage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

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