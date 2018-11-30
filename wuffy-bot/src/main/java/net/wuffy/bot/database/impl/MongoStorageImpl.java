package net.wuffy.bot.database.impl;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.database.DBExtension;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.DBMember;
import net.wuffy.bot.database.DBUser;
import net.wuffy.core.database.mongo.MongoBulkWriteSystem;
import net.wuffy.core.database.mongo.MongoStorage;
import net.wuffy.core.lang.II18n;

public class MongoStorageImpl extends MongoBulkWriteSystem implements DBExtension {

	private Map<Long, DBGuild> guilds = new HashMap<Long, DBGuild>();
	private Map<Long, DBUser> users = new HashMap<Long, DBUser>();

	private Map<Long, Map<Long, DBMember>> members = new HashMap<Long, Map<Long, DBMember>>();

	private II18n i18n;

	private MongoStorage storage;

	@Override
	protected void registered(MongoStorage storage) {
		this.storage = storage;

		this.i18n = new MongoDBI18n(this.getCore(), this.storage);
	}

	@Override
	public DBGuild getGuild(Guild guild) {
		long guildId = guild.getIdLong();

		if(!this.guilds.containsKey(guildId))
			this.guilds.put(guildId, new MongoDBGuild(this.storage, this.getCore(), guild));

		return this.guilds.get(guildId);
	}

	@Override
	public DBMember getMember(Member member) {
		long guildId = member.getGuild().getIdLong();
		long userId = member.getUser().getIdLong();

		if(!this.members.containsKey(guildId))
			this.members.put(guildId, new HashMap<Long, DBMember>());

		if(!this.members.get(guildId).containsKey(userId))
			this.members.get(guildId).put(userId, new MongoDBMember(this.getCore(), member, this.getGuild(member.getGuild()), this.getUser(member.getUser())));

		return this.members.get(guildId).get(userId);
	}

	@Override
	public DBUser getUser(User user) {
		long userId = user.getIdLong();

		if(!this.users.containsKey(userId))
			this.users.put(userId, new MongoDBUser(this.storage, this.getCore(), user));

		return this.users.get(userId);
	}

	@Override
	public II18n getI18n() {
		return this.i18n;
	}

	public MongoStorage getStorage() {
		return this.storage;
	}
}