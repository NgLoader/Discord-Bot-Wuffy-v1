package net.wuffy.bot.module;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.wuffy.bot.database.DBExtension;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.DBMember;
import net.wuffy.bot.database.DBUser;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.IWuffyPhantomReference;

public abstract class Module implements IWuffyPhantomReference {

	public abstract void onEnable();
	public abstract void onDisable();

	protected abstract void onDestroy();

	protected EnumModuleType type;
	protected GuildModule guildModule;

	public Module(EnumModuleType type, GuildModule shardModule) {
		this.type = type;
		this.guildModule = shardModule;

		WuffyPhantomRefernce.getInstance().add(this, String.format("Module - %s - %s", this.guildModule.getGuild().getJDA().getShardInfo().getShardString(), this.type.name()));
	}

	public void destroy() {
		this.unregisterListener(true);
		this.unregisterListener(false);
		this.unregisterTickable();

		try {
			this.onDisable();
		} catch(Exception e) {
			Logger.fatal("Module", String.format("Error by disable module \"%s\" for guild \"%s\"",
					this.type != null ?
						this.type.name() : "UNKNOWN",
					this.guildModule != null && this.guildModule.getGuild().getJDA() != null && this.guildModule.getGuild().getJDA().getShardInfo() != null ?
						this.guildModule.getGuild().getJDA().getShardInfo().getShardString() : "UNKNOWN"),
					e);
		}
		try {
			this.onDestroy();
		} catch(Exception e) {
			Logger.fatal("Module", String.format("Error by destroying module \"%s\" for guild \"%s\"",
					this.type != null ?
						this.type.name() : "UNKNOWN",
					this.guildModule != null && this.guildModule.getGuild().getJDA() != null && this.guildModule.getGuild().getJDA().getShardInfo() != null ?
						this.guildModule.getGuild().getJDA().getShardInfo().getShardString() : "UNKNOWN"),
					e);
		}

		this.guildModule = null;
		this.type = null;

		Logger.debug("Module", String.format("Destroyed module \"%s\" for guild \"%s\"",
				this.type != null ?
					this.type.name() : "UNKNOWN",
				this.guildModule != null && this.guildModule.getGuild().getJDA() != null && this.guildModule.getGuild().getJDA().getShardInfo() != null ?
					this.guildModule.getGuild().getJDA().getShardInfo().getShardString() : "UNKNOWN"));
	}

	public void registerListener(boolean global) {
		if(global)
			this.guildModule.getGuild().getJDA().addEventListener(this);
		else
			this.guildModule.getGuildModuleListenerAdapter().register(this);
	}

	public void unregisterListener(boolean global) {
		if(global)
			this.guildModule.getGuild().getJDA().removeEventListener(this);
		else
			this.guildModule.getGuildModuleListenerAdapter().unregister(this);
	}

	public void registerTickable() {
		if(this instanceof ITickable)
			this.guildModule.getGuild().getCore().addTickable((ITickable) this);
	}

	public void unregisterTickable() {
		if(this instanceof ITickable)
			this.guildModule.getGuild().getCore().removeTickable((ITickable) this);
	}

	public DBGuild getGuild() {
		return this.guildModule.getGuild();
	}

	public DBGuild getGuild(Guild guild) {
		return this.getGuild().getCore().getStorageService().getStorage().getProvider(DBExtension.class).getGuild(guild);
	}

	public DBMember getMember(Member member) {
		return this.getGuild().getCore().getStorageService().getStorage().getProvider(DBExtension.class).getMember(member);
	}

	public DBUser getUser(User user) {
		return this.getGuild().getCore().getStorageService().getStorage().getProvider(DBExtension.class).getUser(user);
	}

	public GuildModule getGuildModule() {
		return this.guildModule;
	}

	public EnumModuleType getType() {
		return this.type;
	}
}