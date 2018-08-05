package de.ngloader.bot.command.commands.fun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import de.ngloader.core.logger.Logger;
import de.ngloader.core.util.WebRequestBuilder;
import net.dv8tion.jda.core.entities.Message;
import okhttp3.Request;
import okhttp3.Response;

@Command(aliases = { "ascii" })
@CommandConfig(category = CommandCategory.FUN)
public class CommandAscii extends BotCommand {

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
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		I18n i18n = event.getCore().getI18n();
		String locale = event.getMember(WuffyMember.class).getLocale();

		if(event.getMember(WuffyMember.class).hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_ASCII)) {
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

					this.replay(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_ASCII_LIST, locale,
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
										ReplayBuilder.queue(event, MessageType.INFO, message.editMessage(i18n.format(TranslationKeys.MESSAGE_ASCII_MINIMIZED, locale,
												"%l", String.format("http://patorjk.com/software/taag/#p=display&f=%s&t=%s", font, text))));
									else
										ReplayBuilder.queue(event, MessageType.INFO, message.editMessage(String.format("```ascii\n%s\n```", answer)));
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else
								this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_ASCII_FONT_NOT_FOUND, locale,
										"%n", args[0]));
						} else
							this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_ASCII_TEXT_TO_LONG, locale,
									"%n", args[0]));
							
					} else
						this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_ASCII_SYNTAX, locale));
					break;
				}
			} else
				this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_ASCII_SYNTAX, locale));
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale,
					"%p", PermissionKeys.COMMAND_ASCII.key));
	}
}