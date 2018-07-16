package de.ngloader.bot.database.guild.mongo;

import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplGuild;
import de.ngloader.core.database.impl.ImplUser;
import net.dv8tion.jda.core.entities.Member;

public class MongoMember extends WuffyMember {

	protected final MongoExtensionGuild extension;

	public MongoMember(Core core, ImplUser user, Member member, ImplGuild guild, MongoExtensionGuild extension) {
		super(core, user, member, guild);

		this.extension = extension;
	}
}