package net.wuffy.core.database.impl;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.wuffy.core.database.IStorageExtension;

public interface IExtensionGuild <GUILD extends ImplGuild, MEMBER extends ImplMember> extends IStorageExtension {

	public GUILD getGuild(Guild guild);

	public MEMBER getMemeber(Guild guild, Member member);
}