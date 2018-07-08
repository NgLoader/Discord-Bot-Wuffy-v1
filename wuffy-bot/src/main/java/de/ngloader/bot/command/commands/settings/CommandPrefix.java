package de.ngloader.bot.command.commands.settings;

import java.util.List;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@Command(aliases = { "prefix" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandPrefix extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		var executer = event.getMember(WuffyMember.class);
		var guild = event.getGuild(WuffyGuild.class);
		var locale = guild.getLocale();
		var i18n = event.getCore().getI18n();

		if(executer.hasPermission(event.getChannel().getIdLong(), "command.prefix")) {
			if(args.length > 0) {
				List<String> prefixes = guild.getPrefixes();
				if(args[0].equalsIgnoreCase("list")) {

					if(!prefixes.isEmpty()) {
						event.getChannel().sendMessage(i18n.format(TranslationKeys.MESSAGE_PREFIX_LIST, locale,
								"%l", "\n**-**    ``" + String.join("``\n**-**    ``", guild.getPrefixes()) + "``\n")).queue();
					} else
						this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_LIST_EMPTY, locale));
				} else if(args.length > 1) {

					if(args[0].equalsIgnoreCase("add")) {
						if(prefixes.size() < 10) {
							if(prefixes.stream().noneMatch(prefix -> prefix.equalsIgnoreCase(args[1]))) {
								guild.addPrefix(args[1].toLowerCase());
								this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_ADDED, locale, "%p", args[1].toLowerCase()));
							} else
								this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_ALREADY_EXIST, locale));
						} else
							this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_MAX_COUNT, locale));
					} else if(args[0].equalsIgnoreCase("remove")) {
						if(prefixes.size() > 1) {
							if(args[1].length() < 11) {
								guild.removePrefix(args[1].toLowerCase());
								this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_REMOVED, locale, "%p", args[1].toLowerCase()));
							} else
								this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_MAX_LENGTH, locale));
						} else
							this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_MIN_COUNT, locale));
					} else
						this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_FALSE_ARGS, locale));
				} else
					this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_FALSE_ARGS, locale));
			} else
				this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_PREFIX_FALSE_ARGS, locale));
		} else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.prefix"));
	}
}