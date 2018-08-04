package de.ngloader.bot.command.commands.fun;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import net.dv8tion.jda.core.EmbedBuilder;

@Command(aliases = { "coinflip", "cflip", "coinf" })
@CommandConfig(category = CommandCategory.FUN)
public class CommandCoinflip extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_COINFLIP)) {
			if(Math.floor(Math.random() * 100) + 1 > 50) {
				this.replay(event, MessageType.INFO, new EmbedBuilder()
						.setDescription(i18n.format(TranslationKeys.MESSAGE_COINFLIP_HEAD, locale))
						.setImage("https://wuffy.eu/pictures/coinflip/head.png"));
			} else {
				this.replay(event, MessageType.INFO, new EmbedBuilder()
						.setDescription(i18n.format(TranslationKeys.MESSAGE_COINFLIP_TAIL, locale))
						.setImage("https://wuffy.eu/pictures/coinflip/tail.png"));
			}
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_COINFLIP.key));
	}
}