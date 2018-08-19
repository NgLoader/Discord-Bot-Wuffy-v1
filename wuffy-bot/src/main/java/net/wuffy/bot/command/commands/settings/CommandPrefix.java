package net.wuffy.bot.command.commands.settings;

import java.util.List;

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

@CommandSettings(
		category = CommandCategory.SETTINGS,
		memberPermissionList = { PermissionKeys.COMMAND_PREFIX },
		memberPermissionRequierd = { PermissionKeys.COMMAND_PREFIX },
		aliases = { "prefix" })
public class CommandPrefix extends Command {

	public CommandPrefix(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		String locale = member.getLocale();

		if(args.length > 0) {
			List<String> prefixes = guild.getPrefixes();
			if(args[0].equalsIgnoreCase("list")) {

				if(!prefixes.isEmpty()) {
					this.sendMessage(event, MessageType.LIST, i18n.format(
							TranslationKeys.MESSAGE_PREFIX_LIST,
							locale,
							"%l", "\n**-**    ``" + String.join("``\n**-**    ``", guild.getPrefixes()) + "``\n"));
				} else
					this.sendMessage(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_PREFIX_LIST_EMPTY, locale));
			} else if(args.length > 1) {

				if(args[0].equalsIgnoreCase("add")) {
					if(prefixes.size() < 10) {
						if(args[1].length() < 11) {
							if(prefixes.stream().noneMatch(prefix -> prefix.equalsIgnoreCase(args[1]))) {
								guild.addPrefix(args[1].toLowerCase());
								this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PREFIX_ADDED, locale, "%p", args[1].toLowerCase()));
							} else
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PREFIX_ALREADY_EXIST, locale));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PREFIX_MAX_LENGTH, locale));
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PREFIX_MAX_COUNT, locale));
				} else if(args[0].equalsIgnoreCase("remove")) {
//					if(prefixes.size() > 1) {
						if(prefixes.stream().anyMatch(prefix -> prefix.equalsIgnoreCase(args[1]))) {
							guild.removePrefix(args[1].toLowerCase());
							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PREFIX_REMOVED, locale, "%p", args[1].toLowerCase()));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PREFIX_NOT_EXIST, locale));
//					} else
//						this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_PREFIX_MIN_COUNT, locale));
				} else
					this.sendHelpMessage(event, command, args);
			} else
				this.sendHelpMessage(event, command, args);
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}