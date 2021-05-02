package net.wuffy.bot.command.commands.image;

import java.io.IOException;

import org.json.JSONObject;

import net.dv8tion.jda.api.entities.Message;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.common.util.WebRequestBuilder;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;
import okhttp3.Request;
import okhttp3.Response;

@CommandSettings(
		category = CommandCategory.IMAGE,
		memberPermissionList = { PermissionKeys.COMMAND_CAT },
		memberPermissionRequierd = { PermissionKeys.COMMAND_CAT },
		aliases = { "cat", "cats", "meow", "meows" },
		privateChatCommand = true)
public class CommandCat extends Command {

	private static final Request CAT_REQUEST = new Request.Builder()
			.url("https://aws.random.cat/meow")
			.build();

	public CommandCat(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		this.sendImage(event, event.getMember(WuffyMember.class).getLocale());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		this.sendImage(event, event.getAuthor(WuffyUser.class).getUserLocale("en-US"));
	}

	public void sendImage(WuffyMessageRecivedEvent event, String locale) {
		try {
			Message message = event.getChannel().sendMessage(this.createEmbed(event, MessageType.LOADING)
					.appendDescription(i18n.format(TranslationKeys.MESSAGE_IMAGE_SEARCHING, locale))
					.build()).complete();

			Response response = WebRequestBuilder.request(CommandCat.CAT_REQUEST);

			JSONObject json = new JSONObject(response.body().string());

			if(json.has("file")) {
				this.queue(event, MessageType.PICTURE, message.editMessage(this.createEmbed(event, MessageType.PICTURE).setImage(json.getString("file")).build()));
			} else
				this.queue(event, MessageType.SYNTAX, message.editMessage(this.createEmbed(event, MessageType.SYNTAX)
						.appendDescription(this.i18n.format(TranslationKeys.MESSAGE_IMAGE_NOTHING_FOUND, locale)).build()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}