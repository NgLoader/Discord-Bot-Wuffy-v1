package net.wuffy.bot.commandOLD.commands.fun;

import java.time.Instant;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;

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