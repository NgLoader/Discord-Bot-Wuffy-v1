package net.wuffy.bot.command.commands.nsfw;

import java.io.IOException;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

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
		memberPermissionList = { PermissionKeys.COMMAND_YANDERE },
		memberPermissionRequierd = { PermissionKeys.COMMAND_YANDERE },
		aliases = { "yandere" },
		privateChatCommand = true,
		nsfw = true)
public class CommandYandere extends Command {

	private static final Request YANDERE_URL = new Request.Builder()
			.url("https://yande.re/post.json")
			.build();

	private static final Random RANDOM = new Random();

	public CommandYandere(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendImage(event, event.getMember(WuffyMember.class).getLocale());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendImage(event, event.getAuthor(WuffyUser.class).getUserLocale("en-US"));
	}

	public void sendImage(WuffyMessageRecivedEvent event, String locale) {
		try {
			Message message = event.getChannel().sendMessage(this.createEmbed(event, MessageType.LOADING)
					.appendDescription(i18n.format(TranslationKeys.MESSAGE_IMAGE_SEARCHING, locale))
					.build()).complete();

			Response response = WebRequestBuilder.request(CommandYandere.YANDERE_URL);

			JSONArray array = new JSONArray(response.body().string());

			if(array.length() > 0) {
				JSONObject element = array.getJSONObject(CommandYandere.RANDOM.nextInt(array.length()));

				this.queue(event, MessageType.PICTURE, message.editMessage(this.createEmbed(event, MessageType.PICTURE)
						.setImage(element.getString("file_url"))
						.addField(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_SCORE, locale), Integer.toString(element.getInt("score")), true)
						.addField(i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_RATING, locale), element.getString("rating").toUpperCase(), true)
						.build()));
			} else
				this.queue(event, MessageType.SYNTAX, message.editMessage(this.createEmbed(event, MessageType.SYNTAX)
						.appendDescription(this.i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_FOUND, locale)).build()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}