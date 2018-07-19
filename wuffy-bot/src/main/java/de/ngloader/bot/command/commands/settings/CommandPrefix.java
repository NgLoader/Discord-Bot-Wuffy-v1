package de.ngloader.bot.command.commands.settings;

import java.util.List;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;

@Command(aliases = { "prefix" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandPrefix extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_PREFIX)) {
			if(args.length > 0) {
				List<String> prefixes = guild.getPrefixes();
				if(args[0].equalsIgnoreCase("list")) {

					if(!prefixes.isEmpty()) {
						new ReplayBuilder(event, MessageType.LIST, i18n.format(
								TranslationKeys.MESSAGE_PREFIX_LIST,
								locale,
								"%l", "\n**-**    ``" + String.join("``\n**-**    ``", guild.getPrefixes()) + "``\n")).queue();
					} else
						this.replay(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_PREFIX_LIST_EMPTY, locale));
				} else if(args.length > 1) {

					if(args[0].equalsIgnoreCase("add")) {
						if(prefixes.size() < 10) {
							if(args[1].length() < 11) {
								if(prefixes.stream().noneMatch(prefix -> prefix.equalsIgnoreCase(args[1]))) {
									guild.addPrefix(args[1].toLowerCase());
									this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PREFIX_ADDED, locale, "%p", args[1].toLowerCase()));
								} else
									this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PREFIX_ALREADY_EXIST, locale));
							} else
								this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PREFIX_MAX_LENGTH, locale));
						} else
							this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PREFIX_MAX_COUNT, locale));
					} else if(args[0].equalsIgnoreCase("remove")) {
//						if(prefixes.size() > 1) {
							if(prefixes.stream().anyMatch(prefix -> prefix.equalsIgnoreCase(args[1]))) {
								guild.removePrefix(args[1].toLowerCase());
								this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PREFIX_REMOVED, locale, "%p", args[1].toLowerCase()));
							} else
								this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PREFIX_NOT_EXIST, locale));
//						} else
//							this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_PREFIX_MIN_COUNT, locale));
					} else
						this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PREFIX_SYNTAX, locale));
				} else
					this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PREFIX_SYNTAX, locale));
			} else
				this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PREFIX_SYNTAX, locale));
		} else
			this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_PREFIX.key));
	}
}