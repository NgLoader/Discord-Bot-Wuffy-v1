package net.wuffy.bot.commandOLD.commands.image;

import java.io.IOException;

import org.json.JSONObject;

import net.dv8tion.jda.core.entities.Message;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.common.util.WebRequestBuilder;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import okhttp3.Request;
import okhttp3.Response;

@CommandSettings(
		category = CommandCategory.IMAGE,
		memberPermissionList = { PermissionKeys.COMMAND_DOG },
		memberPermissionRequierd = { PermissionKeys.COMMAND_DOG },
		aliases = { "dog", "dogs", "woof", "wuf", "wau" },
		privateChatCommand = true)
public class CommandDog extends Command {

	private static final Request DOG_REQUEST = new Request.Builder()
			.url("https://dog.ceo/api/breeds/image/random")
			.build();

	public CommandDog(CommandHandler handler) {
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

			Response response = WebRequestBuilder.request(CommandDog.DOG_REQUEST);

			JSONObject json = new JSONObject(response.body().string());

			if(json.has("message")) {
				this.queue(event, MessageType.PICTURE, message.editMessage(this.createEmbed(event, MessageType.PICTURE).setImage(json.getString("message")).build()));
			} else
				this.queue(event, MessageType.SYNTAX, message.editMessage(this.createEmbed(event, MessageType.SYNTAX)
						.appendDescription(this.i18n.format(TranslationKeys.MESSAGE_IMAGE_NOTHING_FOUND, locale)).build()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}