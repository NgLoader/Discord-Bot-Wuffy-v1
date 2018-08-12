package de.ngloader.bot.command.commands.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.entities.Role;

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		String locale = member.getLocale();

		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {

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
			} else if(args.length > 1) {
				Role role = DiscordUtil.searchRole(guild, String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

				switch(args[0].toLowerCase()) {
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
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NO_ROLE_FOUND, locale, "%n", args[1]));
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
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NO_ROLE_FOUND, locale, "%n", args[1]));
					break;

				default:
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_SYNTAX, locale));
					break;
				}
			} else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_SYNTAX, locale));
		} else
			this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_SYNTAX, locale));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}