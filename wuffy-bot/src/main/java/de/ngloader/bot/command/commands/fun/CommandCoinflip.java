package de.ngloader.bot.command.commands.fun;

import java.time.Instant;

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
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;

@CommandSettings(
		category = CommandCategory.FUN,
		memberPermissionList = { PermissionKeys.COMMAND_COINFLIP },
		memberPermissionRequierd = { PermissionKeys.COMMAND_COINFLIP },
		aliases = { "coinflip", "flipcoin" },
		privateChatCommand = true)
public class CommandCoinflip extends Command {

	public CommandCoinflip(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendResponse(event, event.getTextChannel());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendResponse(event, event.getPrivateChannel());
	}

	private void sendResponse(WuffyMessageRecivedEvent event, MessageChannel channel) {
		String locale = event.getChannelType() == ChannelType.TEXT ? event.getMember(WuffyMember.class).getLocale() : event.getAuthor(WuffyUser.class).getUserLocale("en-US");

		if(Math.floor(Math.random() * 100) + 1 > 50) {
			this.sendMessage(event, MessageType.INFO, this.createEmbed(event, MessageType.PICTURE)
					.setTimestamp(Instant.now())
					.setDescription(i18n.format(TranslationKeys.MESSAGE_COINFLIP_HEAD, locale))
					.setImage("https://wuffy.eu/pictures/coinflip/head.png").build());
		} else {
			this.sendMessage(event, MessageType.INFO, this.createEmbed(event, MessageType.PICTURE)
					.setTimestamp(Instant.now())
					.setDescription(i18n.format(TranslationKeys.MESSAGE_COINFLIP_NUMBER, locale))
					.setImage("https://wuffy.eu/pictures/coinflip/number.png").build());
		}
	}
}