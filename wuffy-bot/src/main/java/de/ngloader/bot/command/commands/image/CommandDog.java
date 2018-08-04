package de.ngloader.bot.command.commands.image;

import java.io.IOException;
import java.time.Instant;

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

@Command(aliases = { "dog", "dogs", "woof", "wuf", "wau" })
@CommandConfig(category = CommandCategory.IMAGE)
public class CommandDog extends BotCommand {

	private static final Request CAT_REQUEST = new Request.Builder()
			.url("https://dog.ceo/api/breeds/image/random")
			.build();

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_DOG)) {
			try {
				Message message = event.getTextChannel().sendMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
						.setupDefault(false, false)
						.setDescription(i18n.format(TranslationKeys.MESSAGE_FUN_SEARCHING, locale))
						.getEmbedBuilder()
						.build())
					.complete();

				Response response = WebRequestBuilder.request(CommandDog.CAT_REQUEST);

				JSONObject json = new JSONObject(response.body().string());

				if(json.has("message")) {
					ReplayBuilder.queue(event, MessageType.PICTURE, message.editMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
							.setupDefault(false, false)
							.setImage(json.getString("message"))
							.setTimestamp(Instant.now())
						.getEmbedBuilder()
						.build()));
				} else
					ReplayBuilder.queue(event, MessageType.PICTURE, message.editMessage(new ReplayBuilder(event, MessageType.PICTURE, false)
							.setupDefault(false, false)
							.setDescription(i18n.format(TranslationKeys.MESSAGE_FUN_NOTHING_FOUND, locale))
							.setTimestamp(Instant.now())
						.getEmbedBuilder()
						.build()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_DOG.key));
	}
}