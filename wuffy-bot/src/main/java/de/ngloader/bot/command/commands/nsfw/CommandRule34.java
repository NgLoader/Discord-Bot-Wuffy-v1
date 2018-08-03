package de.ngloader.bot.command.commands.nsfw;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

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

@Command(aliases = { "rule34", "role34" })
@CommandConfig(category = CommandCategory.NSFW)
public class CommandRule34 extends BotCommand {

	private static final String RULE34_URL = "http://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=";

	private static final Random RANDOM = new Random();

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_RULE34)) {
			if(event.getTextChannel().isNSFW()) {
				try {
					Message message = event.getTextChannel().sendMessage(new ReplayBuilder(event, MessageType.LIST)
							.setDescription(i18n.format(TranslationKeys.MESSAGE_NSFW_SEARCHING, locale))
							.getEmbedBuilder()
							.build())
						.complete();

					Response response = WebRequestBuilder.request(new Request.Builder()
							.url(String.format("%s%s", CommandRule34.RULE34_URL, args.length > 0 ? String.join("_", Arrays.copyOfRange(args, 0, args.length)) : "random"))
							.build());

					JSONObject json = XML.toJSONObject(response.body().string());
					JSONObject posts = json.getJSONObject("posts");

					if(!posts.isNull("count") && posts.getInt("count") > 0) {
						JSONObject post = null;

						if(posts.getInt("count") > 1) { //When the count is one it return a object and when it is higher then a array...
							JSONArray array = json.getJSONObject("posts").getJSONArray("post");
							post = array.getJSONObject(CommandRule34.RANDOM.nextInt(array.length()));
						} else
							post = posts.getJSONObject("post");

						message.editMessage(new ReplayBuilder(event, MessageType.LIST)
								.setImage(post.getString("file_url"))
								.addField(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_SCORE, locale), Integer.toString(post.getInt("score")), true)
								.addField(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_RATING, locale), post.getString("rating").toUpperCase(), true)
								.setTimestamp(Instant.now())
							.getEmbedBuilder()
							.build())
						.queue();
					} else
						message.editMessage(new ReplayBuilder(event, MessageType.LIST)
								.setDescription(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_FOUND, locale))
								.setTimestamp(Instant.now())
							.getEmbedBuilder()
							.build())
						.queue();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else
				this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NSFW_CHANNEL_NOT, locale));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_RULE34.key));
	}
}