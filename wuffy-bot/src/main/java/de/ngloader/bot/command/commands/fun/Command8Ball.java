package de.ngloader.bot.command.commands.fun;

import java.util.Random;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.FUN,
		memberPermissionList = { PermissionKeys.COMMAND_8BALL },
		memberPermissionRequierd = { PermissionKeys.COMMAND_8BALL },
		aliases = { "8ball" },
		privateChatCommand = true)
public class Command8Ball extends Command {

	public Command8Ball(CommandHandler handler) {
		super(handler);
	}

	private static final Random RANDOM = new Random();

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.send8BallAnswer(event, event.getMember(WuffyMember.class).getLocale());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.send8BallAnswer(event, event.getAuthor(WuffyUser.class).getUserLocale("en-US"));
	}

	private void send8BallAnswer(WuffyMessageRecivedEvent event, String locale) {
		String[] answer = i18n.format(TranslationKeys.MESSAGE_8BALL_ANSWER, locale).split("#");

		if(answer.length > 0) {
			this.sendMessage(event, MessageType.INFO, String.format(":8ball: %s", answer[Command8Ball.RANDOM.nextInt(answer.length)]));
		} else
			this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_8BALL_NO_ANSWER, locale));
	}
}