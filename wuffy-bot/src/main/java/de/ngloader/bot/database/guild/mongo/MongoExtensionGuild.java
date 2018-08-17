package de.ngloader.bot.database.guild.mongo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;

import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.mongo.MongoBulkWriteSystem;
import de.ngloader.core.database.mongo.MongoStorage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class MongoExtensionGuild extends MongoBulkWriteSystem implements IExtensionGuild<MongoGuild, MongoMember> {

	private final Map<Long, MongoGuild> CACHED_GUILDS = new HashMap<Long, MongoGuild>();
	private final Map<Long, Map<Long, MongoMember>> CACHED_MEMBERS = new HashMap<Long, Map<Long, MongoMember>>();

	@Override
	public void registered(MongoStorage storage) {
		this.enableBulkWrite(storage.getCollection("guild"), "Guild");

		AtomicBoolean indexExist = new AtomicBoolean(false);
		this.bulkCollection.listIndexes().forEach(new Block<Document>() {

			@Override
			public void apply(Document t) {
				if(t.get("name", "").equals("_guildId_"))
					indexExist.set(true);
			}
		});

		if(!indexExist.get())
			this.bulkCollection.createIndex(new Document("_guildId", -1L), new IndexOptions().name("_guildId_").unique(true).background(false).sparse(true));
	}

	@Override
	protected void unregistered() {
		this.disableBulkWrite();

		this.CACHED_GUILDS.clear();
		this.CACHED_MEMBERS.clear();
	}

	@Override
	public MongoGuild getGuild(Guild guild) {
		if(guild == null)
			return null;

		var longId = guild.getIdLong();

		if(!CACHED_GUILDS.containsKey(longId))
			CACHED_GUILDS.put(longId, new MongoGuild(this.core, guild, this));

		return CACHED_GUILDS.get(longId);
	}

	@Override
	public MongoMember getMemeber(Guild guild, Member member) {
		if(member == null || member.getUser() == null)
			return null;

		var longId = member.getUser().getIdLong();
		var guildId = guild.getIdLong();

		if(!CACHED_MEMBERS.containsKey(guildId)) {
			CACHED_MEMBERS.put(guildId, new HashMap<Long, MongoMember>());
			CACHED_MEMBERS
				.get(guildId)
				.put(longId, new MongoMember(this.core, this.core.getStorageService().getExtension(IExtensionUser.class).getUser(member.getUser()), member, this.getGuild(guild), this));
		} else if(!CACHED_MEMBERS.get(guildId).containsKey(longId))
			CACHED_MEMBERS
				.get(guildId)
				.put(longId, new MongoMember(this.core, this.core.getStorageService().getExtension(IExtensionUser.class).getUser(member.getUser()), member, this.getGuild(guild), this));

		return CACHED_MEMBERS.get(guildId).get(longId);
	}

	public MongoCollection<Document> getCollection() {
		return this.bulkCollection;
	}
}