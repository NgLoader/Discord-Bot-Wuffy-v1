package de.ngloader.master.command;

import de.ngloader.command.Command;
import de.ngloader.command.CommandResult;
import de.ngloader.command.ICommandExecutor;
import de.ngloader.language.TranslationKeys;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

@Command(trigger = TranslationKeys.UNKNOWN)
public class CommandTest implements ICommandExecutor {

	@Override
	public CommandResult onCommand(GuildMessageReceivedEvent event, String[] args) {
		return null;
	}

	@Override
	public void onHelp(GuildMessageReceivedEvent event) {
	}
}