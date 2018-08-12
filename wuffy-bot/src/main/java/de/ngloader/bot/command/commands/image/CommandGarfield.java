package de.ngloader.bot.command.commands.image;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;

@CommandSettings(
		category = CommandCategory.IMAGE,
		memberPermissionList = { PermissionKeys.COMMAND_GARFIELD },
		memberPermissionRequierd = { PermissionKeys.COMMAND_GARFIELD },
		aliases = { "garfield" },
		privateChatCommand = true)
public class CommandGarfield extends Command {

	private static final String GARFIELD_REQUEST = "https://d1ejxu6vysztl5.cloudfront.net/comics/garfield/";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final Long GARFIELD_EPOCH_DATE = 267058800000L;
	private static final Long ONE_DAY = 86400000L;

	public CommandGarfield(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendImage(event);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendImage(event);
	}

	public void sendImage(WuffyMessageRecivedEvent event) {
		Calendar calendar = new Calendar.Builder().setInstant(Math.round(Math.floor(Math.random() * (System.currentTimeMillis() - ONE_DAY - GARFIELD_EPOCH_DATE) + GARFIELD_EPOCH_DATE))).build();
		//OLD: GARFIELD_EPOCH_DATE + (Currentlydate - 24H - GARFIELD_EPOCH_DATE) (started at 1978-06-19)
		//NEW: Math.round(Math.floor(Math.random() * (CurrentlyTimeMillis - ONE_DAY - GARFIELD_EPOCH_DATE) + GARFIELD_EPOCH_DATE))

		this.sendMessage(event, MessageType.PICTURE, this.createEmbed(event, MessageType.PICTURE)
				.setImage(String.format("%s%s/%s.gif", CommandGarfield.GARFIELD_REQUEST, Integer.toString(calendar.get(Calendar.YEAR)), CommandGarfield.DATE_FORMAT.format(calendar.getTime())))
				.setTimestamp(Instant.ofEpochMilli(calendar.getTimeInMillis())));
	}
}