package net.wuffy.bot.commandOLD.commands.botauthor;

import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

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