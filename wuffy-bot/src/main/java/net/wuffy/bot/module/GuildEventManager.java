package net.wuffy.bot.module;

import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.util.IWuffyPhantomReference;
import net.wuffy.core.event.EventManager;

public class GuildEventManager implements IWuffyPhantomReference {

	private GuildModule guildModule;

	private long guildId;
	private EventManager guildEventManager;

	private boolean registeredListener = false;

	public GuildEventManager(GuildModule guildModule) {
		this.guildModule = guildModule;

		this.guildId = this.guildModule.getGuild().getIdLong();

		this.guildEventManager = new EventManager();

		WuffyPhantomRefernce.getInstance().add(this, String.format("GuildModuleListenerAdapter for guild \"%s\"", Long.toString(this.guildId)));
	}

	@SubscribeEvent
	public void onGenericGuildEvent(GenericGuildEvent event) {
		if(event.getGuild().getIdLong() == this.guildId)
			this.guildEventManager.handle(event);
	}

	public void destroy() {
		this.guildEventManager.getRegisteredListeners().forEach(this::unregister);

		this.guildModule = null;
		this.guildEventManager = null;
	}

	public void register(Object listener) {
		this.guildEventManager.register(listener);

		if(!this.registeredListener && !this.guildEventManager.getRegisteredListeners().isEmpty()) {
			this.registeredListener = true;
			this.guildModule.getGuild().getJDA().addEventListener(this);
		}
	}

	public void unregister(Object listener) {
		this.guildEventManager.unregister(listener);

		if(registeredListener && this.guildEventManager.getRegisteredListeners().isEmpty()) {
			this.registeredListener = false;
			this.guildModule.getGuild().getJDA().removeEventListener(this);
		}
	}

	public GuildModule getGuildModule() {
		return this.guildModule;
	}

	public EventManager getGuildEventManager() {
		return this.guildEventManager;
	}

	public long getGuildId() {
		return this.guildId;
	}
}