package net.wuffy.bot.command.commands.fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

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
import net.wuffy.common.logger.Logger;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.WebRequestBuilder;
import okhttp3.Request;
import okhttp3.Response;

@CommandSettings(
		category = CommandCategory.FUN,
		memberPermissionList = { PermissionKeys.COMMAND_ASCII },
		memberPermissionRequierd = { PermissionKeys.COMMAND_ASCII },
		aliases = { "ascii" },
		privateChatCommand = true)
public class CommandAscii extends Command {

	public CommandAscii(CommandHandler handler) {
		super(handler);
	}

	private static final String URL_FRONTS_LIST = "http://173.249.11.205:8009/?type=list";
	private static final String URL_MAKE = "http://173.249.11.205:8009/?type=make";

	private static final List<String> FRONTS = new ArrayList<String>();

	static {
		try {
			Response response = WebRequestBuilder.request(new Request.Builder()
					.url(CommandAscii.URL_FRONTS_LIST)
					.build());

			JSONObject json = new JSONObject(response.body().string());

			json.getJSONArray("message").forEach(front -> CommandAscii.FRONTS.add(String.valueOf(front).replace(" ", "_")));

			Logger.info("Command Ascii", String.format("Loaded %s fronts.", Integer.toString(CommandAscii.FRONTS.size())));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * ~ascii list [page]
	 * ~ascii <type> <text>
	 */

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.createAscii(event, command, args, event.getMember(WuffyMember.class).getLocale());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.createAscii(event, command, args, event.getAuthor(WuffyUser.class).getUserLocale("en-US"));
	}

	public void createAscii(WuffyMessageRecivedEvent event, String command, String[] args, String locale) {
		if(args.length > 0) {
			switch(args[0].toLowerCase()) {
			case "l":
			case "list":
				int index = 20;
				if(args.length > 1 && args[1].matches("[0-9]{1,3}")) {
					int page = Integer.valueOf(args[1]);

					if(page < 1)
						index = 20;
					else if(page * 20 > CommandAscii.FRONTS.size())
						index = CommandAscii.FRONTS.size();
					else
						index = page * 20;
				}

				this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_ASCII_LIST, locale,
						"%pc", Integer.toString(index / 20),
						"%pm", Integer.toString(CommandAscii.FRONTS.size() / 20),
						"%l", String.format("**%s**", String.join("**\n- **", CommandAscii.FRONTS.subList(index - 20, index)))));
				break;

			default:
				if(args.length > 1) {
					String font = null;
					String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace(" ", "%20");

					if(text.length() < 1500) {
						for(String f : CommandAscii.FRONTS)
							if(f.equalsIgnoreCase(args[0])) {
								font = f.replace("_", "%20");
								break;
							}

						if(font != null) {
							try {
								Message message = event.getTextChannel().sendMessage(i18n.format(TranslationKeys.MESSAGE_IMAGE_SEARCHING, locale)).complete();

								Response response = WebRequestBuilder.request(new Request.Builder()
										.url(String.format("%s&font=%s&text=%s", CommandAscii.URL_MAKE, font, text))
										.build());

								JSONObject json = new JSONObject(response.body().string());

								String answer = json.getString("message");

								if(answer.length() > 1000)
									this.queue(event, MessageType.INFO, message.editMessage(i18n.format(TranslationKeys.MESSAGE_ASCII_MINIMIZED, locale,
											"%l", String.format("http://patorjk.com/software/taag/#p=display&f=%s&t=%s", font, text))));
								else
									this.queue(event, MessageType.INFO, message.editMessage(String.format("```ascii\n%s\n```", answer)));
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_ASCII_FONT_NOT_FOUND, locale,
									"%n", args[0]));
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_ASCII_TEXT_TO_LONG, locale,
								"%n", args[0]));
						
				} else
					this.sendHelpMessage(event, command, args);
				break;
			}
		} else
			this.sendHelpMessage(event, command, args);
	}
}