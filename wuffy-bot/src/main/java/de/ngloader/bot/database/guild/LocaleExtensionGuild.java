package de.ngloader.bot.database.guild;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.locale.LocaleStorage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public class LocaleExtensionGuild extends StorageProvider<LocaleStorage> implements IExtensionGuild<WuffyGuild, WuffyMember> {

	@Override
	public void registered(LocaleStorage storage) {
	}

	@Override
	public WuffyGuild getGuild(Guild guild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WuffyMember getMemeber(Guild guild, Member member) {
		// TODO Auto-generated method stub
		return null;
	}
}