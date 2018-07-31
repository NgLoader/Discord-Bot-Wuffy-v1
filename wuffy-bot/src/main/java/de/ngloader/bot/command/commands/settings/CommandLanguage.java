package de.ngloader.bot.command.commands.settings;

import java.util.Locale;
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
import de.ngloader.core.database.impl.ImplLang;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.LocaleUtil;

@Command(aliases = { "language", "lang" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandLanguage extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
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

						this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_CHANGED_USER, member.getLocale(),
								"%l", i18n.format(TranslationKeys.MESSAGE_LANGUAGE_USER_USE_GUILD, member.getLocale())));
					} else {
						Locale locale = LocaleUtil.getLocaleByTag(args[1]);

						if(locale != null && event.getCore().getI18n().getLang(locale.toLanguageTag()) != null) {
							member.getUser().setUserLocale(locale.toLanguageTag());

							this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_CHANGED_USER, member.getLocale(), "%l", locale.toLanguageTag()));
						} else
							this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_NOT_EXIST, member.getLocale()));
					}
				} else 
					this.replay(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_INFO_USER, member.getLocale(),
							"%l", member.getUser().getUserLocale() == null ?
									i18n.format(TranslationKeys.MESSAGE_LANGUAGE_USER_USE_GUILD, member.getLocale()) : member.getUser().getUserLocale()));
				break;

			case "g":
			case "guild":
				if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_LANGUAGE_GUILD))
					if(args.length > 1) {
						Locale locale = LocaleUtil.getLocaleByTag(args[1]);
	
						if(locale != null && event.getCore().getI18n().getLang(locale.toLanguageTag()) != null) {
							guild.setGuildLocale(locale.toLanguageTag());
	
							this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_CHANGED_GUILD, member.getLocale(), "%l", locale.toLanguageTag()));
						} else
							this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_NOT_EXIST, member.getLocale()));
					} else
						this.replay(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_INFO_GUILD, member.getLocale(), "%l", guild.getGuildLocale()));
				else
					this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, member.getLocale(), "%p", PermissionKeys.COMMAND_LANGUAGE_GUILD.key));
				break;

			case "l":
			case "list":
				this.replay(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_LIST, member.getLocale(), "%l", event.getCore().getI18n().getLangs().entrySet().stream()
						.map(entry -> {
							ImplLang lang = entry.getValue();

							return String.format("``%s`` - **%s**", lang.getLocale().toLanguageTag(), lang.getTranslation(TranslationKeys.LANGUAGE));
						})
						.collect(Collectors.joining("\n"))));
				break;

			case "i":
			case "info":
				this.replay(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_INFO, member.getLocale(),
						"%u", member.getUser().getUserLocale() == null ?
							i18n.format(TranslationKeys.MESSAGE_LANGUAGE_USER_USE_GUILD, member.getLocale()) : member.getUser().getUserLocale(),
						"%g", guild.getGuildLocale()));
				break;

			default:
				this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_SYNTAX, member.getLocale()));
				break;
			}
		} else
			this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LANGUAGE_SYNTAX, member.getLocale()));
	}
}