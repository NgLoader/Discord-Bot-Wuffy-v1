package net.wuffy.core.event.events;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.wuffy.core.event.impl.EventGuild;
import net.wuffy.core.util.ArgumentBuffer;

/**
 * 
 * @author Nils
 *
 * Not useful
 */
@Deprecated
public class WuffyGuildMessageReceivedEvent<T extends EventGuild> extends GuildMessageReceivedEvent {

	private ArgumentBuffer argumentBuffer;

	private T guild;

	public WuffyGuildMessageReceivedEvent(GuildMessageReceivedEvent event) {
		this(event.getJDA(), event.getResponseNumber(), event.getMessage());
	}

	public WuffyGuildMessageReceivedEvent(JDA api, long responseNumber, Message message) {
		super(api, responseNumber, message);

		this.argumentBuffer = new ArgumentBuffer(message.getContentRaw());
	}

	public ArgumentBuffer getArgumentBuffer() {
		return this.argumentBuffer;
	}

	@Override
	public T getGuild() {
		return this.guild;
	}
}