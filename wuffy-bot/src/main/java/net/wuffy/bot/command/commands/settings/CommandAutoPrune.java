package net.wuffy.bot.command.commands.settings;

import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;

@CommandSettings(
		category = CommandCategory.SETTINGS,
		memberPermissionList = { PermissionKeys.COMMAND_AUTOPRUNE },
		memberPermissionRequierd = { PermissionKeys.COMMAND_AUTOPRUNE },
		aliases = { "autoprune", "aprune" })
public class CommandAutoPrune extends Command {

	public CommandAutoPrune(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}