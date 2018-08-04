package de.ngloader.bot.command.commands.image;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;

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

@Command(aliases = { "garfield" })
@CommandConfig(category = CommandCategory.IMAGE)
public class CommandGarfield extends BotCommand {

	private static final String GARFIELD_REQUEST = "https://d1ejxu6vysztl5.cloudfront.net/comics/garfield/";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final Long GARFIELD_EPOCH_DATE = 267058800000L;
	private static final Long ONE_DAY = 86400000L;

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_GARFIELD)) {

			Calendar calendar = new Calendar.Builder().setInstant(Math.round(Math.floor(Math.random() * (System.currentTimeMillis() - ONE_DAY - GARFIELD_EPOCH_DATE) + GARFIELD_EPOCH_DATE))).build();
			//OLD: GARFIELD_EPOCH_DATE + (Currentlydate - 24H - GARFIELD_EPOCH_DATE) (started at 1978-06-19)
			//NEW: Math.round(Math.floor(Math.random() * (CurrentlyTimeMillis - ONE_DAY - GARFIELD_EPOCH_DATE) + GARFIELD_EPOCH_DATE))

			this.replay(event, MessageType.PICTURE, new EmbedBuilder()
					.setImage(String.format("%s%s/%s.gif", CommandGarfield.GARFIELD_REQUEST, Integer.toString(calendar.get(Calendar.YEAR)), CommandGarfield.DATE_FORMAT.format(calendar.getTime())))
					.setTimestamp(Instant.ofEpochMilli(calendar.getTimeInMillis())));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_GARFIELD.key));
	}
}