package net.wuffy.bot.command.commands.settings;

import java.util.Locale;
import java.util.stream.Collectors;

import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.common.util.LocaleUtil;
import net.wuffy.core.database.impl.ImplLang;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.lang.I18n;

@CommandSettings(
		category = CommandCategory.SETTINGS,
		memberPermissionList = { PermissionKeys.COMMAND_LANGUAGE_GUILD },
		aliases = { "language"," lang", "locale" })
public class CommandLanguage extends Command {

	public CommandLanguage(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();

		if(args.length > 0) {
			switch(args[0].toLowerCase()) {
			case "self":
			case "member":
			case "u":
			case "user":
				if(args.length > 1) {
					if(args[1].equalsIgnoreCase("none") ||
							args[1].equalsIgnoreCase("null") ||
							args[1].equalsIgnoreCase("guild") ||
							args[1].equalsIgnoreCase("empty") ||
							args[1].equalsIgnoreCase("-")) {
						member.getUser().setUserLocale(null);

						this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_CHANGED_USER, member.getLocale(),
								"%l", i18n.format(TranslationKeys.MESSAGE_LANGUAGE_USER_USE_GUILD, member.getLocale())));
					} else {
						Locale locale = LocaleUtil.getLocaleByTag(args[1]);

						if(locale != null && event.getCore().getI18n().getLang(locale.toLanguageTag()) != null) {
							member.getUser().setUserLocale(locale.toLanguageTag());

							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_CHANGED_USER, member.getLocale(), "%l", locale.toLanguageTag()));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_NOT_EXIST, member.getLocale()));
					}
				} else 
					this.sendMessage(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_INFO_USER, member.getLocale(),
							"%l", member.getUser().getUserLocale() == null ?
									i18n.format(TranslationKeys.MESSAGE_LANGUAGE_USER_USE_GUILD, member.getLocale()) : member.getUser().getUserLocale()));
				break;

			case "g":
			case "guild":
				if(this.hasMemberPermission(event, PermissionKeys.COMMAND_LANGUAGE_GUILD))
					if(args.length > 1) {
						Locale locale = LocaleUtil.getLocaleByTag(args[1]);
	
						if(locale != null && event.getCore().getI18n().getLang(locale.toLanguageTag()) != null) {
							guild.setGuildLocale(locale.toLanguageTag());
	
							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_CHANGED_GUILD, member.getLocale(), "%l", locale.toLanguageTag()));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_NOT_EXIST, member.getLocale()));
					} else
						this.sendMessage(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_INFO_GUILD, member.getLocale(), "%l", guild.getGuildLocale()));
				break;

			case "l":
			case "list":
				this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_LIST, member.getLocale(), "%l", event.getCore().getI18n().getLangs().entrySet().stream()
						.map(entry -> {
							ImplLang lang = entry.getValue();

							return String.format("``%s`` - **%s**", lang.getLocale().toLanguageTag(), lang.getTranslation(TranslationKeys.LANGUAGE));
						})
						.collect(Collectors.joining("\n"))));
				break;

			case "i":
			case "info":
				this.sendMessage(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_INFO, member.getLocale(),
						"%u", member.getUser().getUserLocale() == null ?
							i18n.format(TranslationKeys.MESSAGE_LANGUAGE_USER_USE_GUILD, member.getLocale()) : member.getUser().getUserLocale(),
						"%g", guild.getGuildLocale()));
				break;

			default:
				this.sendHelpMessage(event, command, args);
				break;
			}
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}