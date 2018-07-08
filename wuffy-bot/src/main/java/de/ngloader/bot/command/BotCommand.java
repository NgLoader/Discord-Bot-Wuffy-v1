package de.ngloader.bot.command;

import java.util.concurrent.TimeUnit;

import de.ngloader.core.command.ICommand;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
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

	public void replay(MessageChannel channel, String message) {
		channel.sendMessage(message).queue(success -> success.delete().queueAfter(10, TimeUnit.SECONDS));
	}
}