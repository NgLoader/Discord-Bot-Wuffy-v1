package net.wuffy.core.event;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.wuffy.core.event.events.WuffyGuildJoinEvent;
import net.wuffy.core.event.events.WuffyGuildLeaveEvent;
import net.wuffy.core.event.events.WuffyGuildMessageReceivedEvent;
import net.wuffy.core.event.events.WuffyReadyEvent;

public abstract class CoreListenerAdapter<T extends EventGuild> implements EventListener {

	public void onGuildMessageReceivedEvent(WuffyGuildMessageReceivedEvent<T> event) { }

	public void onGuildJoinEvent(WuffyGuildJoinEvent<T> event) { }
	public void onGuildLeaveEvent(WuffyGuildLeaveEvent<T> event) { }

	public void onReadyEvent(WuffyReadyEvent<T> event) { }

	@Override
	public void onEvent(Event event) {
		if(event instanceof GuildMessageReceivedEvent)
			this.onGuildMessageReceivedEvent(new WuffyGuildMessageReceivedEvent<T>((GuildMessageReceivedEvent) event));
		else if(event instanceof GuildJoinEvent)
			this.onGuildJoinEvent(new WuffyGuildJoinEvent<T>((GuildJoinEvent) event));
		else if(event instanceof GuildLeaveEvent)
			this.onGuildLeaveEvent(new WuffyGuildLeaveEvent<T>((GuildLeaveEvent) event));
		else if(event instanceof ReadyEvent)
			this.onReadyEvent(new WuffyReadyEvent<T>((ReadyEvent) event));
	}
}