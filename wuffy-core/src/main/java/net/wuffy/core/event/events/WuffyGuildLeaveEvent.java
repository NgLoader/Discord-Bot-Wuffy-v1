package net.wuffy.core.event.events;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.wuffy.core.event.EventGuild;

public class WuffyGuildLeaveEvent<T extends EventGuild> extends GuildLeaveEvent {

	private T guild;

	public WuffyGuildLeaveEvent(GuildLeaveEvent event) {
		this(event.getJDA(), event.getResponseNumber(), event.getGuild());
	}

	public WuffyGuildLeaveEvent(JDA api, long responseNumber, Guild guild) {
		super(api, responseNumber, guild);
	}

	@Override
	public T getGuild() {
		return this.guild;
	}
}