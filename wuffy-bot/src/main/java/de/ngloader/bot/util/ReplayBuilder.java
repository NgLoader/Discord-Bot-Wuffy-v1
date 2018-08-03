package de.ngloader.bot.util;

import java.awt.Color;
import java.time.temporal.TemporalAccessor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyGenericMessageEvent;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

public class ReplayBuilder {

	private static final String EMOTE_ERROR = "<a:error:473423519720538112>";
	private static final String EMOTE_LOADING = "<a:loading:468438447573696522>";
	private static final String EMOTE_SUCCESS = "<a:checkmark:459068723408535552>";

	public static void queue(WuffyMessageRecivedEvent event, MessageType type, MessageAction message) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);

		if(guild.isMessageDeleteExecuter())
			event.getMessage().delete().queue();

		if(guild.isMessageDeleteBot() && guild.isMessageDeleteDelay(type))
			message.queue(success -> {success.delete().queueAfter(guild.getMessageDeleteDelay(type), TimeUnit.SECONDS); System.out.println("HIER");});
		else
			message.queue();
	}

	private final WuffyGenericMessageEvent event;
	private final MessageType type;

	private EmbedBuilder embedBuilder = new EmbedBuilder();

	private TimeUnit deleteTimeUnit;
	private Integer deleteDelay;

	public ReplayBuilder(WuffyGenericMessageEvent event, MessageType type) {
		this(event, type, new EmbedBuilder(), true);
	}

	public ReplayBuilder(WuffyGenericMessageEvent event, MessageType type, EmbedBuilder embedBuilder) {
		this(event, type, embedBuilder, true);
	}

	public ReplayBuilder(WuffyGenericMessageEvent event, MessageType type, MessageEmbed messageEmbed) {
		this(event, type, new EmbedBuilder(messageEmbed), true);
	}

	public ReplayBuilder(WuffyGenericMessageEvent event, MessageType type, String description) {
		this(event, type, new EmbedBuilder().setDescription(description), true);
	}

	public ReplayBuilder(WuffyGenericMessageEvent event, MessageType type, String description, Boolean setup) {
		this(event, type, new EmbedBuilder().setDescription(description), setup);
	}

	public ReplayBuilder(WuffyGenericMessageEvent event, MessageType type, Boolean setup) {
		this(event, type, new EmbedBuilder(), setup);
	}

	public ReplayBuilder(WuffyGenericMessageEvent event, MessageType type, MessageEmbed messageEmbed, Boolean setup) {
		this(event, type, new EmbedBuilder(messageEmbed), setup);
	}

	public ReplayBuilder(WuffyGenericMessageEvent event, MessageType type, EmbedBuilder embedBuilder, Boolean setup) {
		this.event = event;
		this.type = type;

		this.embedBuilder = embedBuilder != null ? embedBuilder : new EmbedBuilder();

		this.setupTypeEmotes();

		if(setup)
			this.setupDefault(true, true);
	}

	public ReplayBuilder setupDefault() {
		return this.setupDefault(true, true);
	}

	public ReplayBuilder setupDefault(Boolean deleteExecuter, Boolean deleteBot) {
		WuffyGuild guild = this.event.getGuild(WuffyGuild.class);

		if(deleteExecuter && guild.isMessageDeleteExecuter())
			this.deleteExecuterMessage();

		if(deleteBot && guild.isMessageDeleteDelay(this.type)) {
			this.deleteTimeUnit = TimeUnit.SECONDS;
			this.deleteDelay = guild.getMessageDeleteDelay(this.type);
		}

		String colorCode = guild.getMessageColorCode(this.type);
		this.embedBuilder.setColor(colorCode != null ? Color.decode(colorCode) : this.type.color);

		return this;
	}

	public ReplayBuilder setupTypeEmotes() {
		switch(this.type) {
		case SUCCESS:
			this.applyEmoteSuccess();
			break;

		case LOADING:
			this.applyEmoteLoading();
			break;

		case ERROR:
			this.applyEmoteEorror();
			break;

		default:
			break;
		}

		return this;
	}

	public ReplayBuilder applyEmoteLoading() {
		this.embedBuilder.setDescription(EMOTE_LOADING + " " + this.embedBuilder.getDescriptionBuilder().toString());

		return this;
	}

	public ReplayBuilder applyEmoteSuccess() {
		this.embedBuilder.setDescription(EMOTE_SUCCESS + " " + this.embedBuilder.getDescriptionBuilder().toString());

		return this;
	}

	public ReplayBuilder applyEmoteEorror() {
		this.embedBuilder.setDescription(EMOTE_ERROR + " " + this.embedBuilder.getDescriptionBuilder().toString());

		return this;
	}

	public ReplayBuilder setImage(String url) {
		this.embedBuilder.setImage(url);

		return this;
	}

	public ReplayBuilder setTimestamp(TemporalAccessor temporal) {
		this.embedBuilder.setTimestamp(temporal);

		return this;
	}

	public ReplayBuilder addDescription(String description) {
		this.embedBuilder.appendDescription(description);

		return this;
	}

	public ReplayBuilder setDescription(String description) {
		this.embedBuilder.setDescription(description);

		return this;
	}

	public ReplayBuilder addField(String name, String value, Boolean inline) {
		this.embedBuilder.addField(name, value, inline);

		return this;
	}

	public ReplayBuilder deleteExecuterMessage() {
		this.event.getTextChannel().getMessageById(event.getMessageId()).queue(success -> success.delete().queue());

		return this;
	}

	public ReplayBuilder deleteAfter(TimeUnit timeUnit, Integer delay) {
		this.deleteTimeUnit = timeUnit;
		this.deleteDelay = delay;

		return this;
	}

	public ReplayBuilder queue() {
		return this.queue(true);
	}

	public ReplayBuilder queue(MessageChannel channel) {
		return this.queue(true, channel);
	}

	public ReplayBuilder queue(Consumer<? super ReplayBuilder> finish) {
		return this.queue(true, success -> finish.accept(this));
	}

	public ReplayBuilder queue(MessageChannel channel, Consumer<? super ReplayBuilder> finish) {
		return this.queue(true, channel, success -> finish.accept(this));
	}

	public ReplayBuilder queue(Boolean deleteMessages) {
		if(deleteMessages && deleteTimeUnit != null && deleteDelay != null)
			event.getChannel().sendMessage(this.embedBuilder.build()).queue(success -> success.delete().queueAfter(this.deleteDelay, this.deleteTimeUnit));
		else
			event.getChannel().sendMessage(this.embedBuilder.build()).queue();

		return this;
	}

	public ReplayBuilder queue(Boolean deleteMessages, MessageChannel channel) {
		if(deleteMessages && deleteTimeUnit != null && deleteDelay != null)
			channel.sendMessage(this.embedBuilder.build()).queue(success -> success.delete().queueAfter(this.deleteDelay, this.deleteTimeUnit));
		else
			channel.sendMessage(this.embedBuilder.build()).queue();

		return this;
	}

	public ReplayBuilder queue(Boolean deleteMessages, Consumer<? super ReplayBuilder> finish) {
		event.getChannel().sendMessage(this.embedBuilder.build()).queue(success -> {
			if(deleteMessages && deleteTimeUnit != null && deleteDelay != null)
				success.delete().queueAfter(this.deleteDelay, this.deleteTimeUnit);

			finish.accept(this);
		});

		return this;
	}

	public ReplayBuilder queue(Boolean deleteMessages, MessageChannel channel, Consumer<? super ReplayBuilder> finish) {
		channel.sendMessage(this.embedBuilder.build()).queue(success -> {
			if(deleteMessages && deleteTimeUnit != null && deleteDelay != null)
				success.delete().queueAfter(this.deleteDelay, this.deleteTimeUnit);

			finish.accept(this);
		});

		return this;
	}

	public EmbedBuilder getEmbedBuilder() {
		return this.embedBuilder;
	}

	public WuffyGenericMessageEvent getEvent() {
		return this.event;
	}

	public MessageType getType() {
		return this.type;
	}
}