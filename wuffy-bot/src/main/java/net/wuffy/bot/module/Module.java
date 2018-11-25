package net.wuffy.bot.module;

import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.IWuffyPhantomReference;
import net.wuffy.core.event.CoreListenerAdapter;
import net.wuffy.core.event.EventGuild;

public abstract class Module extends CoreListenerAdapter<EventGuild> implements IWuffyPhantomReference {

	public abstract void onEnable();
	public abstract void onDisable();

	protected EnumModuleType type;
	protected GuildModule<EventGuild> guildModule;

	public Module(EnumModuleType type, GuildModule<EventGuild> guildModule) {
		this.type = type;
		this.guildModule = guildModule;

		WuffyPhantomRefernce.getInstance().add(this, String.format("Module - %s - %s",
				this.guildModule != null && this.guildModule.getGuild() != null ? Long.toString(this.guildModule.getGuild().getIdLong()) : "UNKNOWN",
				this.type.name()));
	}

	public void registerListener() {
		this.guildModule.getGuild().getCore().getEventManager().register(this);
	}

	public void unregisterListener() {
		this.guildModule.getGuild().getCore().getEventManager().unregister(this);
	}

	public void registerTickable() {
		if(this instanceof ITickable)
			this.guildModule.getGuild().getCore().addTickable((ITickable) this);
	}

	public void unregisterTickable() {
		if(this instanceof ITickable)
			this.guildModule.getGuild().getCore().removeTickable((ITickable) this);
	}

	public GuildModule<EventGuild> getGuildModule() {
		return this.guildModule;
	}

	public EnumModuleType getType() {
		return this.type;
	}
}