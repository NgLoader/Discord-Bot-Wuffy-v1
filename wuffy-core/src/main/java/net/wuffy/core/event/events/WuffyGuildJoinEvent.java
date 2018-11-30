package net.wuffy.core.event.events;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.wuffy.core.event.impl.EventGuild;

/**
 * 
 * @author Nils
 *
 * Not useful
 */
@Deprecated
public class WuffyGuildJoinEvent<T extends EventGuild> extends GuildJoinEvent {

	private T guild;

	public WuffyGuildJoinEvent(GuildJoinEvent event) {
		this(event.getJDA(), event.getResponseNumber(), event.getGuild());
	}

	public WuffyGuildJoinEvent(JDA api, long responseNumber, Guild guild) {
		super(api, responseNumber, guild);
	}

	@Override
	public T getGuild() {
		return this.guild;
	}
}