package de.ngloader.core.event;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.IExtensionGuild;
import de.ngloader.core.database.impl.IExtensionUser;
import de.ngloader.core.database.impl.ImplMember;
import de.ngloader.core.database.impl.ImplUser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;

/**
 * @author DV8FromTheWorld
 * <p>{@link net.dv8tion.jda.core.events.message.MessageReceivedEvent MessageReceivedEvent}
 * 
 * @Changes
 * - Changed User to IWuffyUser
 * <br>- Changed Member to IWuffyMember
 */
public class WuffyMessageRecivedEvent extends WuffyGenericMessageEvent {

	private final Message message;

	public WuffyMessageRecivedEvent(Core core, JDA api, long responseNumber, Message message) {
		super(core, api, responseNumber, message.getIdLong(), message.getChannel());
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public ImplUser getAuthor() {
		return this.core.getStorageService().getExtension(IExtensionUser.class).getUser(this.message.getAuthor());
	}

	public <T extends ImplUser> T getAuthor(Class<T> userClass) {
		return userClass.cast(this.getAuthor());
	}

	public ImplMember getMember() {
		return isFromType(ChannelType.TEXT) && !isWebhookMessage()
				? this.core.getStorageService().getExtension(IExtensionGuild.class).getMemeber(this.message.getGuild(), this.message.getMember()) : null;
	}

	public <T extends ImplMember> T getMember(Class<T> memeberClass) {
		return memeberClass.cast(this.getMember());
	}

	public boolean isWebhookMessage() {
		return getMessage().isWebhookMessage();
	}
}