package net.wuffy.bot.module;

import net.dv8tion.jda.core.JDA;
import net.wuffy.core.event.CoreListenerAdapter;
import net.wuffy.core.event.EventGuild;
import net.wuffy.core.event.events.WuffyGuildJoinEvent;
import net.wuffy.core.event.events.WuffyGuildLeaveEvent;
import net.wuffy.core.event.events.WuffyReadyEvent;

public class ModuleListenerAdapter extends CoreListenerAdapter<EventGuild> {//TODO call this in JDA instance (onReady event)

	private JDA jda;

	public ModuleListenerAdapter(JDA jda) {
		this.jda = jda;
	}

	@Override
	public void onReadyEvent(WuffyReadyEvent<EventGuild> event) {
	}

	@Override
	public void onGuildJoinEvent(WuffyGuildJoinEvent<EventGuild> event) {
	}

	@Override
	public void onGuildLeaveEvent(WuffyGuildLeaveEvent<EventGuild> event) {
	}
}