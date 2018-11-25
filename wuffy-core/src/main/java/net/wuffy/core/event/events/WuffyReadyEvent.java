package net.wuffy.core.event.events;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.wuffy.core.event.EventGuild;

public class WuffyReadyEvent<T extends EventGuild> extends ReadyEvent {

	public WuffyReadyEvent(ReadyEvent event) {
		this(event.getJDA(), event.getResponseNumber());
	}

	public WuffyReadyEvent(JDA api, long responseNumber) {
		super(api, responseNumber);
	}
}