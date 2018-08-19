package net.wuffy.bot.database.guild.mongo;

import net.dv8tion.jda.core.entities.Member;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.core.Core;
import net.wuffy.core.database.impl.ImplGuild;
import net.wuffy.core.database.impl.ImplUser;

public class MongoMember extends WuffyMember {

	protected final MongoExtensionGuild extension;

	public MongoMember(Core core, ImplUser user, Member member, ImplGuild guild, MongoExtensionGuild extension) {
		super(core, user, member, guild);

		this.extension = extension;
	}
}