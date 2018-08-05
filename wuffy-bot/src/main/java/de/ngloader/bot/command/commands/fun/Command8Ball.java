package de.ngloader.bot.command.commands.fun;

import java.util.Random;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.bot.util.ReplayBuilder;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;

@Command(aliases = { "8ball" })
@CommandConfig(category = CommandCategory.FUN)
public class Command8Ball extends BotCommand {

	private static final Random RANDOM = new Random();

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_8BALL)) {
			String[] answer = i18n.format(TranslationKeys.MESSAGE_8BALL_ANSWER, locale).split("#");

			if(answer.length > 0) {
				new ReplayBuilder(event, MessageType.INFO, String.format(":8ball: %s", answer[Command8Ball.RANDOM.nextInt(answer.length)]), false)
				.setupDefault(false, true)
				.queue();
			} else
				this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_8BALL_NO_ANSWER, locale));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_8BALL.key));
	}
}