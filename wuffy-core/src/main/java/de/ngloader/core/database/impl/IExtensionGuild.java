package de.ngloader.core.database.impl;

import de.ngloader.core.database.IStorageExtension;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

public interface IExtensionGuild <GUILD extends ImplGuild, MEMBER extends ImplMemeber> extends IStorageExtension {

	public GUILD getGuild(Guild guild);

	public MEMBER getMemeber(Guild guild, Member member);
}