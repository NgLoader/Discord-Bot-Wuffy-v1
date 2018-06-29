package de.ngloader.core.database.impl;

import de.ngloader.core.database.IStorageExtension;

public interface IExtensionGuild <GUILD extends ImplGuild, MEMBER extends ImplMemeber> extends IStorageExtension {

	public GUILD getGuild(Long guildId);

	public MEMBER getMemeber(Long guildId, Long memberId);
}