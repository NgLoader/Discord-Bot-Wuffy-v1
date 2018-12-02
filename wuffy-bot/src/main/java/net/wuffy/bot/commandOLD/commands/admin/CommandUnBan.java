package net.wuffy.bot.commandOLD.commands.admin;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild.Ban;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.ADMIN,
		guildPermissionRequierd = { Permission.BAN_MEMBERS },
		memberPermissionList = { PermissionKeys.COMMAND_UNBAN },
		memberPermissionRequierd = { PermissionKeys.COMMAND_UNBAN },
		aliases = { "unban" })
public class CommandUnBan extends Command {

	public CommandUnBan(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		String locale = member.getLocale();

		if(args.length > 0) {
			Long userId = null;
			if(args[0].matches("<@[0-9]{14,20}>"))
				userId = Long.parseLong(args[0].substring(2, args[0].length() - 1));

			for(Ban ban : event.getGuild().getBanList().complete()) {
				if((userId != null && userId == ban.getUser().getIdLong()) || ban.getUser().getName().equalsIgnoreCase(args[0])) {

					//TODO change in history member was unbanned

					event.getGuild().getController().unban(ban.getUser()).queue();

					this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_UNBAN, locale, "%u", String.format("<@%s>", Long.toString(ban.getUser().getIdLong()))));
					return;
				}
			}

			this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[0]));
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}