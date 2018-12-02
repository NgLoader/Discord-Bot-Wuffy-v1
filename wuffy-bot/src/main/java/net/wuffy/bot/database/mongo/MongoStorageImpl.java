package net.wuffy.bot.database.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.database.DBExtension;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.DBMember;
import net.wuffy.bot.database.DBUser;
import net.wuffy.bot.database.mongo.impl.MongoDBGuild;
import net.wuffy.bot.database.mongo.impl.MongoDBI18n;
import net.wuffy.bot.database.mongo.impl.MongoDBMember;
import net.wuffy.bot.database.mongo.impl.MongoDBUser;
import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.mongo.MongoStorage;
import net.wuffy.core.lang.II18n;

public class MongoStorageImpl extends StorageProvider<MongoStorage> implements DBExtension {

	private Map<Long, DBGuild> guilds = new HashMap<Long, DBGuild>();
	private Map<Long, DBUser> users = new HashMap<Long, DBUser>();

	private Map<Long, Map<Long, DBMember>> members = new HashMap<Long, Map<Long, DBMember>>();

	private II18n i18n;

	private MongoStorage storage;

	private MongoBulkWriteSystemAdapter bulkWriteSystemAdapter;

	@Override
	protected void registered(MongoStorage storage) {
		this.storage = storage;

		this.bulkWriteSystemAdapter = new MongoBulkWriteSystemAdapter();

		this.i18n = new MongoDBI18n(this, this.getCore());
		this.i18n.initialize();
	}

	@Override
	public DBGuild getGuild(Guild guild) {
		long guildId = guild.getIdLong();

		if(!this.guilds.containsKey(guildId))
			this.guilds.put(guildId, new MongoDBGuild(this, this.getCore(), guild));

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
			this.users.put(userId, new MongoDBUser(this, this.getCore(), user));

		return this.users.get(userId);
	}

	@Override
	public II18n getI18n() {
		return this.i18n;
	}

	@Override
	public void clearCache() {
		this.clearCacheGuild();
		this.clearCacheMember();
		this.clearCacheUser();
		this.clearCacheI18n();
	}

	//TODO Module cached DBGuid
	@Override
	public void clearCacheGuild() {
		List<DBGuild> guilds = new ArrayList<DBGuild>(this.guilds.values());

		this.guilds.clear();

		guilds.forEach(DBGuild::destroy);

		guilds.clear();
		guilds = null;
	}

	//TODO Module cached DBMember, DBGuid
	@Override
	public void clearCacheMember() {
		Collection<Map<Long, DBMember>> members = this.members.values();

		this.members.clear();

		members.forEach(map -> map.values().forEach(DBMember::destroy));

		members.clear();
		members = null;
	}

	//TODO Module cached DBMember
	@Override
	public void clearCacheUser() {
		List<DBUser> users = new ArrayList<DBUser>(this.users.values());

		this.users.clear();

		users.forEach(DBUser::destroy);

		users.clear();
		users = null;
	}

	@Override
	public void clearCacheI18n() {
		II18n i18n = this.i18n;

		II18n i18nNew = new MongoDBI18n(this, this.getCore());
		i18nNew.initialize();

		this.i18n = i18nNew;

		i18n.destroy();
		i18n = null;
		i18nNew = null;
	}

	public MongoStorage getStorage() {
		return this.storage;
	}

	public MongoBulkWriteSystemAdapter getBulkWriteSystemAdapter() {
		return this.bulkWriteSystemAdapter;
	}
}