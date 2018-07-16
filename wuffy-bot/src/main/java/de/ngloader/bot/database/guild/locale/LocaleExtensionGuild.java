package de.ngloader.bot.database.guild.locale;

import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.mongo.MongoMember;
import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.locale.LocaleStorage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class LocaleExtensionGuild extends StorageProvider<LocaleStorage> implements IExtensionGuild<WuffyGuild, MongoMember> {

	@Override
	public void registered(LocaleStorage storage) {
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