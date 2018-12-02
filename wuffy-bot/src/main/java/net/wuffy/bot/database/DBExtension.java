package net.wuffy.bot.database;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.wuffy.core.database.IExtension;
import net.wuffy.core.lang.II18n;

public interface DBExtension extends IExtension {

	public DBGuild getGuild(Guild guild);

	public DBMember getMember(Member member);

	public DBUser getUser(User user);

	public II18n getI18n();

	public void clearCache();
	public void clearCacheGuild();
	public void clearCacheMember();
	public void clearCacheUser();
	public void clearCacheI18n();
}