package de.ngloader.bot.command;

import de.ngloader.bot.util.ReplayBuilder;
import de.ngloader.core.command.ICommand;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyGenericMessageEvent;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;

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
}