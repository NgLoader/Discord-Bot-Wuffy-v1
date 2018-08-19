package net.wuffy.bot.command.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

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