package de.ngloader.master.command;

import de.ngloader.api.command.Command;
import de.ngloader.api.command.CommandResult;
import de.ngloader.api.command.ICommandExecutor;
import de.ngloader.api.database.impl.guild.IWuffyGuild;
import de.ngloader.api.database.impl.user.IWuffyUser;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@Command(accountType = AccountType.BOT, aliases = { "test" })
public class CommandTest implements ICommandExecutor {

	@Override
	public CommandResult onCommand(MessageReceivedEvent event, IWuffyGuild guild, IWuffyUser user, String[] args) {
		event.getChannel().sendMessage(String.format("test '%s'", String.join(", ", args))).queue();
		return CommandResult.SUCCESS;
	}
}