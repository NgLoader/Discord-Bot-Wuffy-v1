package de.ngloader.bot.command.commands.botauthor;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.BOT_AUTHOR,
		guildPermissionRequierd = { },
		memberPermissionRequierd = { },
		memberPermissionList = { },
		aliases = { "test", "t" })
public class CommandTest extends Command {

	public CommandTest(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.queue(event, MessageType.HELP, event.getTextChannel().sendMessage(this.buildHelpMessage(event, command, args).build()));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}