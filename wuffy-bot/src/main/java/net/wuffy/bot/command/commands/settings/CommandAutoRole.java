package net.wuffy.bot.command.commands.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Role;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;
import net.wuffy.core.util.DiscordUtil;

@CommandSettings(
		category = CommandCategory.SETTINGS,
		memberPermissionList = { PermissionKeys.COMMAND_AUTOROLE },
		memberPermissionRequierd = { PermissionKeys.COMMAND_AUTOROLE },
		aliases = { "autorole", "arole" })
public class CommandAutoRole extends Command {

	/*
	 * ~autorole list
	 * ~autorole add <Role>
	 * ~autorole remove <Role>
	 */

	public CommandAutoRole(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		String locale = member.getLocale();

		if(args.getSize() > 0) {
			if(args.get(0).equalsIgnoreCase("list") || args.get(0).equalsIgnoreCase("l")) {

				if(!guild.getAutoRole().isEmpty()) {
					List<String> roles = new ArrayList<String>();

					for(String string : guild.getAutoRole()) {
						Role found = guild.getRoleById(string);

						if(found != null)
							roles.add(found.getName());
						else
							guild.removeAutoRole(string);
					}

					this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_LIST, locale,
							"%l", String.format("    - **%s**", roles.stream().collect(Collectors.joining("**\n    - **")))));
				} else
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NO_ROLE_ADDED, locale));
			} else if(args.getSize() > 1) {
				Role role = DiscordUtil.searchRole(guild, String.join(" ", Arrays.copyOfRange(args.getArguments(), 1, args.getSize())));

				switch(args.get(0).toLowerCase()) {
				case "a":
				case "add":
					if(role != null) {
						if(!guild.getAutoRole().contains(role.getId())) {
							if(guild.getAutoRole().size() < 10) {
								guild.addAutoRole(role.getId());

								this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_ADDED, locale, "%n", role.getName()));
							} else
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_MAX_COUNT, locale, "%n", role.getName()));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_ALREADY_ADDED, locale, "%n", role.getName()));
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NO_ROLE_FOUND, locale, "%n", args.get(1)));
					break;

				case "r":
				case "rem":
				case "remove":
					if(role != null) {
						if(guild.getAutoRole().contains(role.getId())) {
							guild.removeAutoRole(role.getId());

							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_REMOVED, locale, "%n", role.getName()));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NOT_ADDED, locale, "%n", role.getName()));
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NO_ROLE_FOUND, locale, "%n", args.get(1)));
					break;

				default:
					this.sendHelpMessage(event, command, args);
					break;
				}
			} else
				this.sendHelpMessage(event, command, args);
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}