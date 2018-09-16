package net.wuffy.bot.command.commands;

import java.awt.Color;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Message.Attachment;
import net.dv8tion.jda.core.entities.Message.MentionType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.requests.RestAction;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

public class MessageBuilder {

	protected WuffyMessageRecivedEvent event;

	protected MessageType messageType;

	protected StringBuilder contentBuilder;
	protected EmbedBuilder embedBuilder;

	protected TextChannel textChannel;

	/**
	 * Default null
	 * 
	 * It will be set when the message was successful send.
	 */
	protected Message message = null;

	protected List<RestAction<?>> restActions = new ArrayList<RestAction<?>>();

	public MessageBuilder(WuffyMessageRecivedEvent event, MessageType messageType) {
		this(event, messageType, event.getTextChannel());
	}

	public MessageBuilder(WuffyMessageRecivedEvent event, MessageType messageType, TextChannel textChannel) {
		this.event = event;
		this.messageType = messageType;
		this.textChannel = textChannel;

		this.textChannel = this.event.getTextChannel();
	}

	public void destroy() {
		this.event = null;
		this.messageType = null;
		this.contentBuilder = null;
		this.embedBuilder.clear();
		this.embedBuilder = null;
		this.message = null;
	}

	public EmbedBuilder createEmbed() {
		WuffyGuild guild = this.event.getChannelType() == ChannelType.TEXT ? this.event.getGuild(WuffyGuild.class) : null;
		EmbedBuilder embedBuilder = new EmbedBuilder();

		switch(this.messageType) {
		case SUCCESS:
			embedBuilder.setDescription(Command.EMOTE_SUCCESS).appendDescription(" ");
			break;

		case LOADING:
			embedBuilder.setDescription(Command.EMOTE_LOADING).appendDescription(" ");
			break;

		case ERROR:
			embedBuilder.setDescription(Command.EMOTE_ERROR).appendDescription(" ");
			break;

		default:
			break;
		}

		embedBuilder.setTimestamp(Instant.now());

		if(this.event.getChannelType() == ChannelType.TEXT)
			embedBuilder.setFooter(
					String.format("%s#%s", this.event.getMember().getEffectiveName(), this.event.getAuthor().getDiscriminator()),
					this.event.getAuthor().getEffectiveAvatarUrl());
		else
			embedBuilder.setFooter(
					String.format("%s#%s", this.event.getAuthor().getName(), this.event.getAuthor().getDiscriminator()),
					this.event.getAuthor().getEffectiveAvatarUrl());

		if(guild != null) {
			String colorCode = guild.getMessageColorCode(this.messageType);
			embedBuilder.setColor(colorCode != null ? Color.decode(colorCode) : this.messageType.color);
		} else
			embedBuilder.setColor(this.messageType.color);

		return embedBuilder;
	}

	/* Message */

	public long getIdLong() {
		return this.message.getIdLong();
	}

	public MessageBuilder formatTo(Formatter formatter, int flags, int width, int precision) {
		this.message.formatTo(formatter, flags, width, precision);
		return this;
	}

	public List<User> getMentionedUsers() {
		return this.message.getMentionedUsers();
	}

	public List<TextChannel> getMentionedChannels() {
		return this.message.getMentionedChannels();
	}

	public List<Role> getMentionedRoles() {
		return this.message.getMentionedRoles();
	}

	public List<Member> getMentionedMembers(Guild guild) {
		return this.message.getMentionedMembers(guild);
	}

	public List<Member> getMentionedMembers() {
		return this.message.getMentionedMembers();
	}

	public List<IMentionable> getMentions(MentionType... types) {
		return this.message.getMentions(types);
	}

	public boolean isMentioned(IMentionable mentionable, MentionType... types) {
		return this.message.isMentioned(mentionable, types);
	}

	public boolean mentionsEveryone() {
		return this.message.mentionsEveryone();
	}

	public boolean isEdited() {
		return this.message.isEdited();
	}

	public OffsetDateTime getEditedTime() {
		return this.message.getEditedTime();
	}

	public User getAuthor() {
		return this.message.getAuthor();
	}

	public Member getMember() {
		return this.message.getMember();
	}

	public String getContentDisplay() {
		return this.message.getContentDisplay();
	}

	public String getContentRaw() {
		return this.message.getContentRaw();
	}

	public String getContentStripped() {
		return this.message.getContentStripped();
	}

	public List<String> getInvites() {
		return this.message.getInvites();
	}

	public String getNonce() {
		return this.message.getNonce();
	}

	public boolean isFromType(ChannelType type) {
		return this.message.isFromType(type);
	}

	public ChannelType getChannelType() {
		return this.message.getChannelType();
	}

	public boolean isWebhookMessage() {
		return this.message.isWebhookMessage();
	}

	public MessageChannel getChannel() {
		return this.message.getChannel();
	}

	public PrivateChannel getPrivateChannel() {
		return this.message.getPrivateChannel();
	}

	public Group getGroup() {
		return this.message.getGroup();
	}

	public TextChannel getTextChannel() {
		return this.message.getTextChannel();
	}

	public Category getCategory() {
		return this.message.getCategory();
	}

	public Guild getGuild() {
		return this.message.getGuild();
	}

	public List<Attachment> getAttachments() {
		return this.message.getAttachments();
	}

	public List<MessageEmbed> getEmbeds() {
		return this.message.getEmbeds();
	}

	public List<Emote> getEmotes() {
		return this.message.getEmotes();
	}

	public List<MessageReaction> getReactions() {
		return this.message.getReactions();
	}

	public boolean isTTS() {
		return this.message.isTTS();
	}

	public boolean isPinned() {
		return this.message.isPinned();
	}

	public net.dv8tion.jda.core.entities.MessageType getType() {
		return this.message.getType();
	}

	/* RestAction<Message> */

	public MessageBuilder queue() {
	}

	public MessageBuilder queue(Consumer<? super Message> success) {
	}

	public MessageBuilder queue(Consumer<? super Message> success, Consumer<? super Throwable> failure) {
		this.textChannel.sendMessage(this.embedBuilder.build());
		
		return this;
	}

	public ScheduledFuture<?> queueAfter(long delay, TimeUnit unit) {
		return super.queueAfter(delay, unit);
	}

	public ScheduledFuture<?> queueAfter(long delay, TimeUnit unit, Consumer<? super Message> success) {
		return super.queueAfter(delay, unit, success);
	}

	public ScheduledFuture<?> queueAfter(long delay, TimeUnit unit, Consumer<? super Message> success, Consumer<? super Throwable> failure) {
		return super.queueAfter(delay, unit, success, failure);
	}

	public ScheduledFuture<?> queueAfter(long delay, TimeUnit unit, Consumer<? super Message> success, Consumer<? super Throwable> failure, ScheduledExecutorService executor) {
		this.message
		return super.queueAfter(delay, unit, success, failure, executor);
	}

	public ScheduledFuture<?> queueAfter(long delay, TimeUnit unit, Consumer<? super Message> success, ScheduledExecutorService executor) {
		return super.queueAfter(delay, unit, success, executor);
	}

	public ScheduledFuture<?> queueAfter(long delay, TimeUnit unit, ScheduledExecutorService executor) {
		return super.queueAfter(delay, unit, executor);
	}

	public Message complete() {
		this.message = super.complete();
		return this.message;
	}

	public Message complete(boolean shouldQueue) throws RateLimitedException {
		this.message = super.complete(shouldQueue);
		return this.message;
	}

	public Message completeAfter(long delay, TimeUnit unit) {
		this.message =  super.completeAfter(delay, unit);
		return this.message;
	}

	public WuffyMessageRecivedEvent getEvent() {
		return this.event;
	}

	public MessageType getMessageType() {
		return this.messageType;
	}

	public Message getMessage() {
		return this.message;
	}
}