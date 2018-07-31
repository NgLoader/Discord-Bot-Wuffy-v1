package de.ngloader.bot.command.commands.settings;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bson.Document;

import de.ngloader.bot.WuffyBot;
import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.NotificationInfo;
import de.ngloader.bot.database.NotificationType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.bot.util.ReplayBuilder;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.GsonUtil;
import de.ngloader.core.util.WebhookUtil;
import de.ngloader.core.youtube.YoutubePart;
import net.dv8tion.jda.core.entities.Webhook;

@Command(aliases = { "notification", "noti", "notifier", "notifications" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandNotification extends BotCommand {

	private static final Document DEFAULT_EMBED_DOCUMENT = new Document("title", "DEFAULT");
    public final static Pattern URL_PATTERN = Pattern.compile("\\s*(https?|attachment)://.+\\..{2,}\\s*", Pattern.CASE_INSENSITIVE);

	/*
	 * ~notification <Type> list
	 * ~notification <Type> add <Name>
	 * ~notification <Type> remove <Name>
	 * ~notification <Type> message <Name> print
	 * ~notification <Type> message <Name> reset
	 * ~notification <Type> message <Name> replacements
	 * ~notification <Type> message <Name> copy <FromName>
	 * ~notification <Type> message <Name> set <message, username, avatar_url, embed> ...
	 * ~notification <Type> message <Name> remove <message, username, avatar_url, embed> ...
	 */

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_NOTIFICATION)) {
			if(args.length > 1) {
				NotificationType notificationType =
						(args[0].equalsIgnoreCase("youtube") ||
						args[0].equalsIgnoreCase("yt") ||
						args[0].equalsIgnoreCase("yout") ||
						args[0].equalsIgnoreCase("ytube") ||
						"youtube".startsWith(args[0].toLowerCase())) ? NotificationType.YOUTUBE :

						(args[0].equalsIgnoreCase("twitch") ||
						args[0].equalsIgnoreCase("twitchtv") ||
						args[0].equalsIgnoreCase("tt") ||
						args[0].equalsIgnoreCase("tw") ||
						"twitchtv".startsWith(args[0].toLowerCase())) ? NotificationType.TWITCH : null;

				if(notificationType != null) {
					switch(args[1].toLowerCase()) {
					case "l":
					case "list":
						if(notificationType == NotificationType.TWITCH)
							new ReplayBuilder(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_LIST, locale,
									"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
									"%l", "\n**-**    ``" + guild.getNotifications(notificationType).stream().map(notification -> notification.name).collect(Collectors.joining("``\n**-**    ``")) + "``\n"))
							.queue();
						else
							new ReplayBuilder(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_LIST, locale,
									"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_YOUTUBE, locale),
									"%l", "\n**-**    ``" + guild.getNotifications(NotificationType.YOUTUBE).stream()
										.map(notification -> String.format("%s (`%s`)", notification.name, notification.channelId))
										.collect(Collectors.joining("``\n**-**    ``")) + "``\n"))
							.queue();
						break;

					case "a":
					case "add":
						if(args.length > 2) {
							String name = args[2];

							if(NotificationType.TWITCH == notificationType ? name.matches("^(#)?[a-zA-Z0-9]{4,25}$") : name.matches("[a-z,A-Z,0-9,\\-,_,',.]{6,20}")) {
								if(guild.getNotifications(notificationType).size() < 10) {
									if(guild.getNotification(notificationType, name.toLowerCase()) == null) {
										if(notificationType == NotificationType.TWITCH) {
											List<Webhook> webhooks = event.getTextChannel().getWebhooks().complete();
											Webhook webhook = null;

											for(Webhook wh : webhooks)
												if(wh.getName().equals("Wuffy Webhook")) {
													webhook = wh;
													break;
												}

											if(webhook == null)
												webhook = event.getTextChannel().createWebhook("Wuffy Webhook").complete();

											guild.addNotification(notificationType, new NotificationInfo(name.toLowerCase(), webhook.getUrl(),
													i18n.format(NotificationType.TWITCH == notificationType ?
															TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_TWITCH : TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_YOUTUBE, locale)));

											new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_ADDED, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
											.queue();
										} else {
											String channelId = null;

											if(args.length > 3) {
												Matcher matcher = Pattern.compile("UC[a-zA-Z0-9-_]{22}").matcher(args[3]);

												if(matcher.find())
													channelId = matcher.group();
											}

											List<String> found = channelId != null ? null :
												event.getCore(WuffyBot.class).getYoutube().getChannelHandler().getByUsername(name, YoutubePart.ID).getItems().stream()
													.map(item -> item.getId())
													.collect(Collectors.toList());

											if(channelId != null || (found != null && !found.isEmpty())) {
												List<Webhook> webhooks = event.getTextChannel().getWebhooks().complete();
												Webhook webhook = null;

												for(Webhook wh : webhooks)
													if(wh.getName().equals("Wuffy Webhook")) {
														webhook = wh;
														break;
													}

												if(webhook == null)
													webhook = event.getTextChannel().createWebhook("Wuffy Webhook").complete();

												guild.addNotification(NotificationType.YOUTUBE, new NotificationInfo(name.toLowerCase(), webhook.getUrl(),
														i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_YOUTUBE, locale), channelId != null ? channelId : found.get(0)));

												new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_ADDED, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_YOUTUBE, locale),
														"%n", name))
												.queue();
											} else
												new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_CHANNEL_NOT_FOUND, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_YOUTUBE, locale),
														"%n", name))
												.queue();
										}
									} else
										new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_ALREADY_EXIST, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
										.queue();
								} else
									new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MAX_COUNT, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
									.queue();
							} else
								new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_ADDED_SYNTAX, locale,
										"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
								.queue();
						} else
							new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
									"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
							.queue();
						break;
	
					case "r":
					case "rem":
					case "remove":
						if(args.length > 2) {
							String name = args[2];
	
							if(guild.getNotification(notificationType, name.toLowerCase()) != null) {
								guild.removeNotification(notificationType, name);
	
								new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_REMOVED, locale,
										"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
										"%n", name))
								.queue();
							} else
								new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_EXIST, locale,
										"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
								.queue();
						} else
							new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
									"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
							.queue();
						break;
	
					case "m":
					case "msg":
					case "message":
						if(args.length > 3) {
							NotificationInfo notificationInfo = guild.getNotification(notificationType, args[2]);
	
							if(notificationInfo != null) {
								Document document = Document.parse(notificationInfo.message != null && !notificationInfo.message.isEmpty() ?
										notificationInfo.message :
										i18n.format(NotificationType.TWITCH == notificationType ?
												TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_TWITCH : TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_YOUTUBE, locale));
	
								switch(args[3].toLowerCase()) {
								case "print":
									if(args.length > 3 && args[4].equalsIgnoreCase("json")) {
										new ReplayBuilder(event, MessageType.LIST, GsonUtil.GSON_PRETTY_PRINTING.toJson(document)).queue();
									} else {
										Webhook webhook = null;
										boolean deleteWebhook = false;
	
										for(Webhook wh : event.getTextChannel().getWebhooks().complete())
											if(wh.getName().startsWith("Wuffy Webhook")) {
												webhook = wh;
												break;
											}
	
										if(webhook == null) {
											webhook = event.getTextChannel().createWebhook("Wuffy Webhook - PRINT").complete();
											deleteWebhook = true;
										}
	
										String response = WebhookUtil.send(webhook.getUrl(),
												notificationType == NotificationType.TWITCH ?
														document.toJson()
															.replace("%vc", Integer.toString(0))
															.replace("%n", notificationInfo.name)
															.replace("%urll", String.format("https://twitch.tv/%s", notificationInfo.name))
															.replace("%t", "Title")
															.replace("%urlt300x300", "https://wuffy.eu/pictures/example_thumbnail_300x300.png")
															.replace("%urlt580x900", "https://wuffy.eu/pictures/example_thumbnail_320x180.png")
															.replace("%urlpi", "https://wuffy.eu/pictures/example_profile_image_320x900.png")
															.replace("%urloi", "https://wuffy.eu/pictures/example_offline_image_320x900.png")
															.replace("%sa", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(System.currentTimeMillis() - 7200000))) // - 2h
															.replace("%g", "IRL")
															.replace("%urlg300x300", "https://wuffy.eu/pictures/example_box_art_url_300x300.png")
															.replace("%urlg580x900", "https://wuffy.eu/pictures/example_box_art_url_320x180.png")
													:
														document.toJson()
															.replace("%ct", notificationInfo.name)
															.replace("%ti", "Title")
															.replace("%pa", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(System.currentTimeMillis() - 7200000)))
															.replace("%d", "Description")
															.replace("%u", String.format("https://www.youtube.com/watch?v=%s", notificationInfo.channelId))
															.replace("%td300x300", "https://wuffy.eu/pictures/example_box_art_url_300x300.png")
															.replace("%tm300x300", "https://wuffy.eu/pictures/example_box_art_url_300x300.png")
															.replace("%th300x300", "https://wuffy.eu/pictures/example_box_art_url_300x300.png")
															.replace("%td580x900", "https://wuffy.eu/pictures/example_box_art_url_320x180.png")
															.replace("%tm580x900", "https://wuffy.eu/pictures/example_box_art_url_320x180.png")
															.replace("%th580x900", "https://wuffy.eu/pictures/example_box_art_url_320x180.png")
												);
	
										if(!response.isEmpty())
											new ReplayBuilder(event, MessageType.ERROR, "Response: " + response);
	
										if(deleteWebhook)
											webhook.delete().queue();
									}
									break;
	
								case "reset":
									guild.setNotificationMessage(notificationType, notificationInfo.name, i18n.format(NotificationType.TWITCH == notificationType ?
											TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_TWITCH : TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_YOUTUBE, locale));
									new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_RESET, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
									.queue();
									break;
	
								case "rep":
								case "replace":
								case "replacement":
								case "replacements":
									new ReplayBuilder(event, MessageType.LIST, i18n.format(NotificationType.TWITCH == notificationType ?
											TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_REPLACEMENTS_TWITCH : TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_REPLACEMENTS_YOUTUBE, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
									.queue();
									break;
	
								case "c":
								case "copy":
									if(args.length > 4) {
										NotificationInfo notificationInfoFrom = guild.getNotification(notificationType, args[4]);
										if(notificationInfoFrom != null) {
											new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_COPY, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
													"%f", notificationInfoFrom.name, //FROM
													"%t", notificationInfo.name)) //TO
											.queue();
										} else
											new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_EXIST, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
											.queue();
									} else
										new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
										.queue();
									break;
	
								case "s":
								case "set":
									if(args.length > 4) {
										String value = args.length > 5 ? args[5] : "";
	
										switch(args[4].toLowerCase()) {
										case "u":
										case "un":
										case "usern":
										case "username":
											if(value.matches("^(#)?[a-zA-Z0-9]{4,25}$")) {
												document.put("username", value);
												guild.setNotificationMessage(notificationType, notificationInfo.name, document.toJson());
	
												new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_USERNAME, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
														"%n", value))
												.queue();
											} else
												new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_URL, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
												.queue();
											break;
	
										case "a":
										case "avatar":
											if(value.length() < 100) {
												document.put("avatar_url", value);
												guild.setNotificationMessage(notificationType, notificationInfo.name, document.toJson());
	
												new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_AVATAR, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
														"%u", value))
												.queue();
											} else
												new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
												.queue();
											break;
	
										case "c":
										case "content":
											if(args.length > 5) {
												if(String.join(" ", Arrays.copyOfRange(args, 5, args.length)).length() < 100) {
													document.put("content", String.join(" ", Arrays.copyOfRange(args, 5, args.length)));
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_CONTENT, locale,
															"%c", String.join(" ", Arrays.copyOfRange(args, 5, args.length))))
													.queue();
												} else
													new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
											} else
												new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_CONTENT_NO_ARGS, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
												.queue();
											break;
	
										case "e":
										case "embed":
											if(args.length > 5) {
												value = args[6];
												Document embed = document.get("embeds", Arrays.asList(CommandNotification.DEFAULT_EMBED_DOCUMENT)).get(0);
	
												switch (args[5].toLowerCase()) {
												case "title":
													if(String.join(" ", Arrays.copyOfRange(args, 6, args.length)).length() < 100) {
														embed.put("title", String.join(" ", Arrays.copyOfRange(args, 6, args.length)));
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%t", String.join(" ", Arrays.copyOfRange(args, 6, args.length))))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "desc":
												case "description":
													if(String.join(" ", Arrays.copyOfRange(args, 6, args.length)).length() < 100) {
														embed.put("description", String.join(" ", Arrays.copyOfRange(args, 6, args.length)));
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_DESCRIPTION, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%d", String.join(" ", Arrays.copyOfRange(args, 6, args.length))))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "url":
													if(value.length() < 100) {
														embed.put("url", value);
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_URL, locale,
																"%u", value))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "color":
													if(value.length() < 100) {
														String color = null;
	
														//Check RGB
														if(args.length > 7) {
															if(value.matches("[0-9]{1,3}") && args[7].matches("[0-9]{1,3}") && args[8].matches("[0-9]{1,3}") &&
																	Integer.valueOf(value) < 256 && Integer.valueOf(args[7]) < 256 && Integer.valueOf(args[8]) < 256)
															color = this.convertColorToHex(new Color(Integer.valueOf(value), Integer.valueOf(args[7]), Integer.valueOf(args[8])));
	
														//Check default
														} else if(value.equalsIgnoreCase("default") || value.equalsIgnoreCase("d"))
															color = "6570405";
	
														//Check hex
														else if(!(color = (value.startsWith("#") ? value : String.format("#%s", value))).matches(CommandMessage.HEX_PATTERN))
															color = null;
	
														//Check color names
														if(color == null)
															for(Map.Entry<String, Color> entry : CommandMessage.COLORS.entrySet())
																if(value.equalsIgnoreCase(entry.getKey())) {
																	color = this.convertColorToHex(entry.getValue());
																	break;
																}
	
														try {
															if(color != null && Color.decode(color) != null) {
																embed.put("color", Integer.parseInt(this.convertColorToHex(Color.decode(color)).substring(1), 16));
																guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
																new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_COLOR, locale,
																		"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																		"%c", color))
																.queue();
																break;
															}
														} catch(NumberFormatException ex) {
														}
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_COLOR_NOT_FOUND, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "ficonurl":
												case "footericonurl":
													if(value.length() < 100) {
														if(!embed.containsKey("footer"))
															embed.put("footer", new Document("icon_url", value));
														else
															embed.get("footer", new Document()).put("icon_url", value);
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%u", value))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "footer":
													if(String.join(" ", Arrays.copyOfRange(args, 6, args.length)).length() < 100) {
														if(!embed.containsKey("footer"))
															embed.put("footer", new Document("text", String.join(" ", Arrays.copyOfRange(args, 6, args.length))));
														else
															embed.get("footer", new Document()).put("text", String.join(" ", Arrays.copyOfRange(args, 6, args.length)));
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%t", String.join(" ", Arrays.copyOfRange(args, 6, args.length))))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "thumbnail":
													if(value.length() < 100) {
														if(!embed.containsKey("thumbnail"))
															embed.put("thumbnail", new Document("url", value));
														else
															embed.get("thumbnail", new Document()).put("url", value);
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%u", value))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "image":
													if(value.length() < 100) {
														if(!embed.containsKey("image"))
															embed.put("image", new Document("url", value));
														else
															embed.get("image", new Document()).put("url", value);
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%u", value))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "authorname":
													if(String.join(" ", Arrays.copyOfRange(args, 6, args.length)).length() < 100) {
														if(!embed.containsKey("author"))
															embed.put("author", new Document("name", String.join(" ", Arrays.copyOfRange(args, 6, args.length))));
														else
															embed.get("author", new Document()).put("name", String.join(" ", Arrays.copyOfRange(args, 6, args.length)));
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%n", value))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "authorurl":
													if(value.length() < 100) {
														if(!embed.containsKey("author"))
															embed.put("author", new Document("url", value));
														else
															embed.get("author", new Document()).put("url", value);
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%u", value))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "authoriconurl":
													if(value.length() < 100) {
														if(!embed.containsKey("author"))
															embed.put("author", new Document("icon_url", value));
														else
															embed.get("author", new Document()).put("icon_url", value);
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																"%u", value))
														.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												case "field":
												case "fields":
													if(value.length() < 100) {
														if(args.length > 8) {
															value = args[6];
															if(value.equalsIgnoreCase("add")) {
																if(embed.get("fields", new ArrayList<Document>()).size() < 6) {
																	List<Document> fields = embed.get("fields", new ArrayList<Document>());
																	fields.add(new Document("name", args[7])
																			.append("value", args[8])
																			.append("inline", Boolean.valueOf(args[9])));
																	embed.put("fields", fields);
																	guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
																	new ReplayBuilder(event, MessageType.SUCCESS,
																			i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																					"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																					"%n", args[7]))
																	.queue();
																} else
																	new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_FIELD_MAX_COUNT, locale,
																			"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
																	.queue();
															} else if(value.equalsIgnoreCase("set")) {
																if(args.length > 9) {
																	boolean found = false;
	
																	for(Document field : embed.get("fields", new ArrayList<Document>())) {
																		if(field.containsKey("name") && field.getString("name").equalsIgnoreCase(args[7])) {
																			List<Document> fields = embed.get("fields", new ArrayList<Document>());
																			fields.set(fields.indexOf(field), new Document("name", args[8])
																					.append("value", args[9])
																					.append("inline", Boolean.valueOf(args[10])));
																			embed.put("fields", fields);
																			guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
																			new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_FIELD_SET, locale,
																					"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																					"%n", field.getString("name")))
																			.queue();
	
																			found = true;
																			break;
																		}
																	}
																	if(!found)
																		new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_EMBED_FIELD_NOT_EXIST, locale,
																				"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																				"%f", args[7])).queue();
																} else
																	new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
																			"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
																	.queue();
															} else
																new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
																		"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
																.queue();
														} else
															new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
																	"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
															.queue();
													} else
														new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													break;
	
												default:
													new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
												}
											} else
												new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
												.queue();
											break;
	
										default:
											new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
											.queue();
											break;
										}
									} else
										new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
										.queue();
									break;
	
								case "r":
								case "remove":
								case "rem":
								case "uset":
								case "unset":
									switch(args[4].toLowerCase()) {
									case "avatar":
										document.remove("avatar_url");
										guild.setNotificationMessage(notificationType, notificationInfo.name, document.toJson());
	
										new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_TITLE, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
										.queue();
										break;
	
									case "content":
										if(document.containsKey("embeds")) {
											document.remove("content");
											guild.setNotificationMessage(notificationType, notificationInfo.name, document.toJson());
	
											new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_TITLE, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
											.queue();
										} else
											new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_REMOVE_CONTENT_OR_EMBED, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
											.queue();
										break;
	
									case "embed":
										Document embed = document.get("embeds", Arrays.asList(CommandNotification.DEFAULT_EMBED_DOCUMENT)).get(0);
										if(document.containsKey("content")) {
											if(args.length < 6) {
												document.remove("embeds");
												guild.setNotificationMessage(notificationType, notificationInfo.name, document.toJson());
	
												new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
												.queue();
											} else
												switch(args[5].toLowerCase()) {
												case "title":
													embed.remove("title");
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_TITLE, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "description":
													embed.remove("description");
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_DESCRIPTION, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "url":
													embed.remove("url");
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_URL, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "color":
													embed.remove("color");
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_COLOR, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "footertext":
													embed.put("footer", embed.get("footer", new Document()).remove("text"));
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FOOTER_TEXT, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "ficonurl":
												case "footericonurl":
													embed.put("footer", embed.get("footer", new Document()).remove("icon_url"));
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FOOTER_ICON_URL, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "footer":
													embed.remove("footer");
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FOOTER, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "author":
													embed.remove("author");
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_AUTHOR, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "authorname":
													embed.put("author", embed.get("author", new Document()).remove("name"));
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_AUTHOR_NAME, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "authorurl":
													embed.put("author", embed.get("author", new Document()).remove("url"));
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_AUTHOR_URL, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "authoriconurl":
													embed.put("author", embed.get("author", new Document()).remove("icon_url"));
													guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
													new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_AUTHOR_ICON_URL, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
	
												case "field":
												case "fields":
													if(args.length < 7) {
														embed.remove("fields");
														guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
														new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FIELD_REMOVE_ALL, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
														.queue();
													} else {
														boolean found = false;
	
														for(Document field : embed.get("fields", new ArrayList<Document>())) {
															if(field.containsKey("name") && field.getString("name").equalsIgnoreCase(args[6])) {
																List<Document> fields = embed.get("fields", new ArrayList<Document>());
																fields.remove(field);
																embed.put("fields", fields);
																guild.setNotificationMessage(notificationType, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());
	
																new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FIELD_REMOVE, locale,
																		"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																		"%n", field.getString("name")))
																.queue();
	
																found = true;
																break;
															}
														}
	
														if(!found)
															new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_EMBED_FIELD_NOT_EXIST, locale,
																	"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale),
																	"%f", args[6])).queue();
													}
													break;
	
												default:
													new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
													.queue();
													break;
												}
										} else
											new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_REMOVE_CONTENT_OR_EMBED, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
											.queue();
										
										break;
	
									default:
										new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
										.queue();
										break;
									}
									break;
	
								default:
									new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
									.queue();
									break;
								}
							} else
								new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_EXIST, locale,
										"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
								.queue();
						} else
							new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
									"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
							.queue();
						break;
	
					default:
						new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
								"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN.replace("unknown", notificationType.name().toLowerCase()), locale)))
						.queue();
						break;
					}
				} else
					new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_SYNTAX, locale,
							"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN, locale)))
					.queue();
			} else
				new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_EXIST_TYPE, locale,
						"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN, locale)))
				.queue(); 
		} else
			new ReplayBuilder(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_NOTIFICATION.key,
					"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_UNKNOWN, locale)))
			.queue();
	}

	private String convertColorToHex(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
}