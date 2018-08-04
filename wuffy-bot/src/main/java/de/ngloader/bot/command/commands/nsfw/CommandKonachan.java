package de.ngloader.bot.command.commands.nsfw;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

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
import de.ngloader.core.util.WebRequestBuilder;
import net.dv8tion.jda.core.entities.Message;
import okhttp3.Request;
import okhttp3.Response;

@Command(aliases = { "konachan" })
@CommandConfig(category = CommandCategory.NSFW)
public class CommandKonachan extends BotCommand {

	private static final Request KONACHAN_URL = new Request.Builder()
			.url("https://konachan.com/post.json")
			.build();

	private static final Random RANDOM = new Random();

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_KONACHAN)) {
			if(event.getTextChannel().isNSFW()) {
				try {
					Message message = event.getTextChannel().sendMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
							.setupDefault(false, false)
							.setDescription(i18n.format(TranslationKeys.MESSAGE_NSFW_SEARCHING, locale))
							.getEmbedBuilder()
							.build())
						.complete();

					Response response = WebRequestBuilder.request(CommandKonachan.KONACHAN_URL);

					JSONArray array = new JSONArray(response.body().string());

					if(array.length() > 0) {
						JSONObject element = array.getJSONObject(CommandKonachan.RANDOM.nextInt(array.length()));

						ReplayBuilder.queue(event, MessageType.PICTURE, message.editMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
								.setupDefault(false, false)
								.setImage(element.getString("file_url"))
								.addField(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_SCORE, locale), Integer.toString(element.getInt("score")), true)
								.addField(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_RATING, locale), element.getString("rating").toUpperCase(), true)
								.setTimestamp(Instant.now())
							.getEmbedBuilder()
							.build()));
					} else
						ReplayBuilder.queue(event, MessageType.PICTURE, message.editMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
								.setupDefault(false, false)
								.setDescription(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_FOUND, locale))
								.setTimestamp(Instant.now())
							.getEmbedBuilder()
							.build()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NSFW_CHANNEL_NOT, locale));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_KONACHAN.key));
	}
}