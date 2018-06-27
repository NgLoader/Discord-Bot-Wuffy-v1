package de.ngloader.core.event;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.IWuffyGuild;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;

public class WuffyGenericMessageEvent extends Event {

	protected final Core core;

	protected final long messageId;
	protected final MessageChannel channel;

	public WuffyGenericMessageEvent(Core core, JDA api, long responseNumber, long messageId, MessageChannel channel) {
		super(api, responseNumber);
		this.core = core;
		this.messageId = messageId;
		this.channel = channel;
	}

	public Core getCore() {
		return core;
	}

	public MessageChannel getChannel() {
		return channel;
	}

	public String getMessageId() {
		return Long.toUnsignedString(messageId);
	}

	public long getMessageIdLong() {
		return messageId;
	}

	public boolean isFromType(ChannelType type) {
		return channel.getType() == type;
	}

	public ChannelType getChannelType() {
		return channel.getType();
	}

	public IWuffyGuild getGuild() {
		return isFromType(ChannelType.TEXT) ? this.core.getGuild(getTextChannel().getGuild().getIdLong()) : null;
	}

	public TextChannel getTextChannel() {
		return isFromType(ChannelType.TEXT) ? (TextChannel) channel : null;
	}

	public PrivateChannel getPrivateChannel() {
		return isFromType(ChannelType.PRIVATE) ? (PrivateChannel) channel : null;
	}

	public Group getGroup() {
		return isFromType(ChannelType.GROUP) ? (Group) channel : null;
	}
}