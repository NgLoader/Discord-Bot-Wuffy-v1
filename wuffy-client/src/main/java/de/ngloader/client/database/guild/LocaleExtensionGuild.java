package de.ngloader.client.database.guild;

import de.ngloader.core.database.StorageProvider;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.locale.LocaleStorage;

public class LocaleExtensionGuild implements StorageProvider<LocaleStorage>, IExtensionGuild<WuffyGuild, WuffyMember> {

	@Override
	public void registered(LocaleStorage storage) {
	}

	@Override
	public WuffyGuild getGuild(Long guildId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WuffyMember getMemeber(Long guildId, Long memberId) {
		// TODO Auto-generated method stub
		return null;
	}
}