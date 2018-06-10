package de.ngloader.master.command;

import de.ngloader.api.command.Command;
import de.ngloader.api.command.CommandResult;
import de.ngloader.api.command.ICommandExecutor;
import de.ngloader.api.database.impl.guild.IWuffyGuild;
import de.ngloader.api.database.impl.user.IWuffyUser;
import de.ngloader.api.lang.TranslationKey;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

@Command(trigger = TranslationKey.UNKNOWN)
public class CommandTest implements ICommandExecutor {

	@Override
	public CommandResult onCommand(GuildMessageReceivedEvent event, IWuffyGuild guild, IWuffyUser user, String[] args) {
		event.getChannel().sendMessage("Test").queue();
		return CommandResult.SUCCESS;
	}
}