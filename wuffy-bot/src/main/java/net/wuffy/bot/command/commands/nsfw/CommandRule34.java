package net.wuffy.bot.command.commands.nsfw;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.WebRequestBuilder;
import okhttp3.Request;
import okhttp3.Response;

@CommandSettings(
		category = CommandCategory.NSFW,
		guildPermissionRequierd = { Permission.MESSAGE_EMBED_LINKS },
		memberPermissionList = { PermissionKeys.COMMAND_RULE34 },
		memberPermissionRequierd = { PermissionKeys.COMMAND_RULE34 },
		aliases = { "rule34" },
		privateChatCommand = true,
		nsfw = true)
public class CommandRule34 extends Command {

	private static final String RULE34_URL = "http://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=";

	private static final Random RANDOM = new Random();

	public CommandRule34(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendImage(event, args, event.getMember(WuffyMember.class).getLocale());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendImage(event, args, event.getAuthor(WuffyUser.class).getUserLocale("en-US"));
	}

	public void sendImage(WuffyMessageRecivedEvent event, String[] args, String locale) {
		try {
			Message message = event.getChannel().sendMessage(this.createEmbed(event, MessageType.LOADING)
					.appendDescription(i18n.format(TranslationKeys.MESSAGE_IMAGE_SEARCHING, locale))
					.build()).complete();

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

				this.queue(event, MessageType.PICTURE, message.editMessage(this.createEmbed(event, MessageType.PICTURE)
						.setImage(post.getString("file_url"))
						.addField(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_SCORE, locale), Integer.toString(post.getInt("score")), true)
						.addField(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_RATING, locale), post.getString("rating").toUpperCase(), true)
						.build()));
			} else
				this.queue(event, MessageType.SYNTAX, message.editMessage(this.createEmbed(event, MessageType.SYNTAX)
						.appendDescription(this.i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_FOUND, locale)).build()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}