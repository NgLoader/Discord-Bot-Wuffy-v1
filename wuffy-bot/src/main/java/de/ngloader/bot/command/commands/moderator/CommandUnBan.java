package de.ngloader.bot.command.commands.moderator;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.entities.Guild.Ban;

@Command(aliases = { "unban", "uban" })
@CommandConfig(category = CommandCategory.MODERATOR)
public class CommandUnBan extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		var executer = event.getMember(WuffyMember.class);
		var locale = event.getGuild(WuffyGuild.class).getLocale();
		var i18n = event.getCore().getI18n();

		if(executer.hasPermission(event.getChannel().getIdLong(), "command.ban"))
			if(args.length > 0) {
				Long userId = null;
				if(args[0].matches("<@[0-9]{14,20}>"))
					userId = Long.parseLong(args[0].substring(2, args[0].length() - 1));

				for(Ban ban : event.getGuild().getBanList().complete()) {
					if((userId != null && userId == ban.getUser().getIdLong()) || ban.getUser().getName().equalsIgnoreCase(args[0])) {
						event.getGuild().getController().unban(ban.getUser()).queue();

						this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_UNBAN, locale, "%u", String.format("<@%s>", Long.toString(ban.getUser().getIdLong()))));
						return;
					}
				}

				this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_UNBAN_NO_MEMBER_FOUND, locale));
			} else
				this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_BAN_FALSE_ARGS, locale));
			//No args
		else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.ban"));
	}
}