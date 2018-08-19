package net.wuffy.bot.database.guild.locale;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.mongo.MongoMember;
import net.wuffy.core.database.StorageProvider;
import net.wuffy.core.database.impl.IExtensionGuild;
import net.wuffy.core.database.locale.LocaleStorage;

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