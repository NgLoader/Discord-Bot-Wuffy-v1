package de.ngloader.bot.command.commands.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.entities.Role;

@Command(aliases = { "autorole", "arole", "autor" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandAutoRole extends BotCommand {

	/*
	 * ~autorole list
	 * ~autorole add <Role>
	 * ~autorole remove <Role>
	 */

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_AUTOROLE)) {
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

						this.replay(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_LIST, locale,
								"%l", String.format("    - **%s**", roles.stream().collect(Collectors.joining("**\n    - **")))));
					} else
						this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NO_ROLE_ADDED, locale));
				} else if(args.length > 1) {
					Role role = DiscordUtil.searchRole(guild, String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

					switch(args[0].toLowerCase()) {
					case "a":
					case "add":
						if(role != null) {
							if(!guild.getAutoRole().contains(role.getId())) {
								if(guild.getAutoRole().size() < 10) {
									guild.addAutoRole(role.getId());

									this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_ADDED, locale, "%n", role.getName()));
								} else
									this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_MAX_COUNT, locale, "%n", role.getName()));
							} else
								this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_ALREADY_ADDED, locale, "%n", role.getName()));
						} else
							this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NO_ROLE_FOUND, locale, "%n", args[1]));
						break;

					case "r":
					case "rem":
					case "remove":
						if(role != null) {
							if(guild.getAutoRole().contains(role.getId())) {
								guild.removeAutoRole(role.getId());

								this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_REMOVED, locale, "%n", role.getName()));
							} else
								this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NOT_ADDED, locale, "%n", role.getName()));
						} else
							this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_NO_ROLE_FOUND, locale, "%n", args[1]));
						break;

					default:
						this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_SYNTAX, locale));
						break;
					}
				} else
					this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_SYNTAX, locale));
			} else
				this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_AUTOROLE_SYNTAX, locale));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_AUTOROLE.key));
	}
}