package de.ngloader.bot.command.commands.settings;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) { }

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}