package net.wuffy.core.event.events;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;
import net.wuffy.core.event.impl.EventGuild;

/**
 * 
 * @author Nils
 *
 * Not useful
 */
@Deprecated
public class WuffyGuildAvailableEvent<T extends EventGuild> extends GuildAvailableEvent {

	private T guild;

	public WuffyGuildAvailableEvent(GuildAvailableEvent event) {
		this(event.getJDA(), event.getResponseNumber(), event.getGuild());
	}

	public WuffyGuildAvailableEvent(JDA api, long responseNumber, Guild guild) {
		super(api, responseNumber, guild);
	}

	@Override
	public T getGuild() {
		return this.guild;
	}
}