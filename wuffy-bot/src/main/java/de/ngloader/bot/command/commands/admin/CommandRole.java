package de.ngloader.bot.command.commands.admin;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.Permission;

@CommandSettings(
		category = CommandCategory.ADMIN,
		guildPermissionRequierd = { Permission.MANAGE_ROLES },
		memberPermissionList = { PermissionKeys.COMMAND_ROLE },
		memberPermissionRequierd = { PermissionKeys.COMMAND_ROLE },
		aliases = { "role" })
public class CommandRole extends Command {

	public CommandRole(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) { }

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}