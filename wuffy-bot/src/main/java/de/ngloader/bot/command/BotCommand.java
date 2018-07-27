package de.ngloader.bot.command;

import java.awt.Color;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.core.command.ICommand;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyGenericMessageEvent;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

public abstract class BotCommand implements ICommand {

	public abstract void execute(WuffyMessageRecivedEvent event, String[] args);

	private boolean commandBlocked;

	public boolean isCommandBlocked() {
		return commandBlocked;
	}

	public void setCommandBlocked(boolean commandBlocked) {
		this.commandBlocked = commandBlocked;
	}

	public ReplayBuilder replay(WuffyGenericMessageEvent event, MessageType type, MessageChannel channel, String message) {
		return new ReplayBuilder(event, type, message).queue(channel);
	}

	public ReplayBuilder replay(WuffyGenericMessageEvent event, MessageType type, String message) {
		return new ReplayBuilder(event, type, message).queue();
	}

	public ReplayBuilder replay(WuffyGenericMessageEvent event, MessageType type, String description, String edit, Integer delay) {
		return new ReplayBuilder(event, type, description).queue(false, success -> success.setDescription(edit).setupTypeEmotes().queue());
	}

	public ReplayBuilder replay(WuffyGenericMessageEvent event, MessageType type, EmbedBuilder builder) {
		return new ReplayBuilder(event, type, builder).queue();
	}

	public EmbedBuilder buildMessage(MessageType type) {
		EmbedBuilder builder = new EmbedBuilder().setColor(type.color);

		if(type == MessageType.SUCCESS)
			builder.setDescription("<a:checkmark:459068723408535552>");
		else if(type == MessageType.LOADING)
			builder.setDescription("<a:loading:468438447573696522>");

		return builder;
	}

	public EmbedBuilder buildMessage(MessageType type, String description) {
		EmbedBuilder builder = new EmbedBuilder().setColor(type.color);

		if(type == MessageType.SUCCESS)
			builder.setDescription("<a:checkmark:459068723408535552> " + description);
		else if(type == MessageType.LOADING)
			builder.setDescription("<a:loading:468438447573696522> " + description);
		else
			builder.setDescription(description);

		return builder;
	}

	public class ReplayBuilder {

		private static final String EMOTE_LOADING = "<a:loading:468438447573696522>";
		private static final String EMOTE_SUCCESS = "<a:checkmark:459068723408535552>";

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

			if(guild.isMessageDeleteExecuter())
				this.deleteExecuterMessage();

			if(guild.isMessageDeleteDelay(this.type)) {
				this.deleteTimeUnit = TimeUnit.SECONDS;
				this.deleteDelay = guild.getMessageDeleteDelay(this.type);
			}

			String colorCode = guild.getMessageColorCode(this.type);
			this.embedBuilder.setColor(colorCode != null ? Color.decode(colorCode) : this.type.color);

			return this;
		}

		public ReplayBuilder setupTypeEmotes() {
			if(type == MessageType.SUCCESS)
				this.applyEmoteSuccess();
			else if(type == MessageType.LOADING)
				this.applyEmoteLoading();
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
			return embedBuilder;
		}

		public WuffyGenericMessageEvent getEvent() {
			return event;
		}

		public MessageType getType() {
			return type;
		}
	}
}