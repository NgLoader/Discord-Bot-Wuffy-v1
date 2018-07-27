package de.ngloader.bot.command.commands.settings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.NotificationInfo;
import de.ngloader.bot.database.NotificationType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.GsonUtil;
import net.dv8tion.jda.core.entities.Webhook;

@Command(aliases = { "tw", "twitch", "twitchtv" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandTwitch extends BotCommand {

	private static final String DEFAULT_EMBED_MESSAGE = "{\"username\": \"Twitch\",\"avatar_url\": \"%urlpi\",\"content\": \"@here %n ist jetzt live auf Twitch\",\"embeds\": [{\"title\": \"%t\",\"url\": \"%urll\",\"color\": 1671309,\"timestamp\": \"%sa\",\"footer\": {\"icon_url\": \"%urlpi\",\"text\": \"%n\"},\"thumbnail\": {\"url\": \"%urlpi\"},\"image\": {\"url\": \"%urlt\"},\"author\": {\"name\": \"%n\",\"url\": \"%urll\",\"icon_url\": \"%urlpi\"},\"fields\": [{\"name\": \"Zuschauer\",\"value\": \"%vc\"}]}]}";

	/*
	 * ~twitch list
	 * ~twitch add <Name>
	 * ~twitch remove <Name>
	 * ~twitch embed <Name> print
	 * ~twitch embed <Name> copy <FromName>
	 * ~twitch embed <Name> edit <message, username, avatar_url, embed> ...
	 */

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_TWTICH)) {
			if(args.length > 0) {
				switch(args[0].toLowerCase()) {
				case "l":
				case "list":
					new ReplayBuilder(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_TWITCH_LIST, locale,
							"%l", "\n**-**    ``" + guild.getNotifications(NotificationType.TWITCH).stream().map(notification -> notification.name).collect(Collectors.joining("``\n**-**    ``")) + "``\n"))
					.queue();
					break;

				case "a":
				case "add":
					if(args.length > 1) {
						String name = args[1];

						if(name.matches("^(#)?[a-zA-Z0-9]{4,25}$")) {
							if(guild.getNotifications(NotificationType.TWITCH).size() < 10) {
								if(guild.getNotification(NotificationType.TWITCH, name.toLowerCase()) == null) {
									List<Webhook> webhooks = event.getTextChannel().getWebhooks().complete();
									Webhook webhook = null;

									for(Webhook wh : webhooks)
										if(wh.getName().startsWith("Wuffy Webhook - ")) {
											webhook = wh;
											break;
										}

									if(webhook == null)
										webhook = event.getTextChannel().createWebhook("Wuffy Webhook - " + event.getTextChannel().getName()).complete();

									guild.addNotification(NotificationType.TWITCH, new NotificationInfo(name.toLowerCase(), webhook.getUrl(), CommandTwitch.DEFAULT_EMBED_MESSAGE));

									new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_TWITCH_ADDED, locale)).queue();
								} else
									new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_TWITCH_ALREADY_EXIST, locale)).queue();
							} else
								new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_TWITCH_MAX_COUNT, locale)).queue();
						} else
							new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_ADDED_SYNTAX, locale)).queue();
					} else
						new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
					break;

				case "r":
				case "rem":
				case "remove":
					if(args.length > 1) {
						String name = args[1];

						if(guild.getNotification(NotificationType.TWITCH, name.toLowerCase()) != null) {
							guild.removeNotification(NotificationType.TWITCH, name);

							new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_TWITCH_REMOVED, locale, "%n", name)).queue();
						} else
							new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_TWITCH_NOT_EXIST, locale)).queue();
					} else
						new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
					break;

				case "m":
				case "msg":
				case "message":
					if(args.length > 2) {
						NotificationInfo notificationInfo = guild.getNotification(NotificationType.TWITCH, args[1]);

						if(notificationInfo != null) {
							Document document = Document.parse(notificationInfo.message != null && !notificationInfo.message.isEmpty() ? notificationInfo.message : CommandTwitch.DEFAULT_EMBED_MESSAGE);

							switch(args[2].toLowerCase()) {
							case "print":
								new ReplayBuilder(event, MessageType.INFO).addDescription(GsonUtil.GSON_PRETTY_PRINTING.toJson(document)).queue();
								break;

							case "reset":
								guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, CommandTwitch.DEFAULT_EMBED_MESSAGE);
								new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_TWITCH_MESSAGE_RESET, locale)).queue();
								break;

							case "c":
							case "copy":
								if(args.length > 3) {
									NotificationInfo notificationInfoFrom = guild.getNotification(NotificationType.TWITCH, args[3]);
									if(notificationInfoFrom != null) {
										new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_TWITCH_MESSAGE_COPY, locale,
												"%f", notificationInfoFrom.name, //FROM
												"%t", notificationInfo.name)).queue(); //TO
									} else
										new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_TWITCH_NOT_EXIST, locale)).queue();
								} else
									new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
								break;

							case "s":
							case "set":
								if(args.length > 3) {
									String value = args[4];

									switch(args[3].toLowerCase()) {
									case "u":
									case "un":
									case "usern":
									case "username":
										if(value.matches("^(#)?[a-zA-Z0-9][\\w]{2,24}$")) {
											document.put("username", value);
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

											new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_MESSAGE_SET_USERNAME, locale)).queue();
										} else
											new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_NOT_URL, locale)).queue();
										break;

									case "a":
									case "avatar":
										if(value.matches("(?:https:\\/\\/)?[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#[\\]@!\\$&'\\(\\)\\*\\+,;=.]+$")) {
											document.put("avatar_url", value);
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

											new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_MESSAGE_SET_AVATAR, locale)).queue();
										} else
											new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_NOT_URL, locale)).queue();
										break;

									case "c":
									case "content":
										document.put("content", String.join(" ", Arrays.copyOfRange(args, 4, args.length)));
										guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

										new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_MESSAGE_SET_CONTENT, locale)).queue();
										break;

									case "e":
									case "embed":
										break;

									default:
										new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
										break;
									}
								} else
									new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
								break;

							case "r":
							case "rem":
							case "remove":
								switch(args[2]) {
								case "avatar":
									break;

								case "content":
									break;

								case "embed":
									break;

								default:
									break;
								}
								break;

							default:
								new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
								break;
							}
						} else
							new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_TWITCH_NOT_EXIST, locale)).queue();
					} else
						new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
					break;

				default:
					new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
					break;
				}
			} else
				new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_TWITCH_SYNTAX, locale)).queue();
		} else
			new ReplayBuilder(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_TWTICH.key)).queue();
	}
}