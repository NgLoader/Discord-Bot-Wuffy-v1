package net.wuffy.bot.commandOLD.commands.settings;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bson.Document;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.Webhook;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.backup.NotificationInfo;
import net.wuffy.bot.database.backup.NotificationType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.common.util.GsonUtil;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.DiscordUtil;
import net.wuffy.core.util.WebhookUtil;

@CommandSettings(
		category = CommandCategory.SETTINGS,
		guildPermissionRequierd = { Permission.MANAGE_WEBHOOKS },
		memberPermissionList = { PermissionKeys.COMMAND_NOTIFICATION_TWITCH },
		memberPermissionRequierd = { PermissionKeys.COMMAND_NOTIFICATION_TWITCH },
		aliases = { "twitch", "tw", "twitchtv" })
public class CommandTwitch extends Command {

	private static final Document DEFAULT_EMBED_DOCUMENT = new Document("title", "DEFAULT");
    public final static Pattern URL_PATTERN = Pattern.compile("\\s*(https?|attachment)://.+\\..{2,}\\s*", Pattern.CASE_INSENSITIVE);

	/*
	 * ~twitch list
	 * ~twitch add <Name> <Channel>
	 * ~twitch remove <Name>
	 * ~twitch message <Name> print
	 * ~twitch message <Name> reset
	 * ~twitch message <Name> replacements
	 * ~twitch message <Name> copy <FromName>
	 * ~twitch message <Name> set <message, username, avatar_url, embed> ...
	 * ~twitch message <Name> remove <message, username, avatar_url, embed> ...
	 */

	public CommandTwitch(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		String locale = member.getLocale();

		if(args.length > 0) {
			switch(args[0].toLowerCase()) {
			case "l":
			case "list":
				this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_LIST, locale,
						"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
						"%l", "\n**-**    ``" + guild.getNotifications(NotificationType.TWITCH).stream().map(notification -> notification.name).collect(Collectors.joining("``\n**-**    ``")) + "``\n"));
				break;

			case "a":
			case "add":
				if(args.length > 1) {
					String name = args[1];
					TextChannel channel = args.length > 2 ? DiscordUtil.searchChannel(guild, args[2]) : event.getTextChannel();

					if(name.matches("^(?!_)\\w{3,15}$")) {
						if(guild.getNotifications(NotificationType.TWITCH).size() < 10) {
							if(guild.getNotification(NotificationType.TWITCH, name.toLowerCase()) == null) {
								if(channel != null) {
									List<Webhook> webhooks = channel.getWebhooks().complete();
									Webhook webhook = null;

									for(Webhook wh : webhooks)
										if(wh.getName().equals("Wuffy Webhook")) {
											webhook = wh;
											break;
										}

									if(webhook == null)
										webhook = event.getTextChannel().createWebhook("Wuffy Webhook").complete();

									guild.addNotification(NotificationType.TWITCH, new NotificationInfo(name.toLowerCase(), webhook.getUrl(),
											i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_TWITCH, locale)));

									this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_ADDED, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
											"%n", name));
								} else
									this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TEXTCHANNEL_NOT_FOUND, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
											"%n", name,
											"%c", args[2]));
							} else
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_ALREADY_EXIST, locale,
										"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
										"%n", name));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MAX_COUNT, locale,
									"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
									"%n", name));
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_ADDED_SYNTAX, locale,
								"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
								"%n", name));
				} else
					this.sendHelpMessage(event, command, args);
				break;

			case "r":
			case "rem":
			case "remove":
				if(args.length > 1) {
					String name = args[1];

					if(guild.getNotification(NotificationType.TWITCH, name.toLowerCase()) != null) {
						guild.removeNotification(NotificationType.TWITCH, name);

						this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_REMOVED, locale,
								"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
								"%n", name));
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_EXIST, locale,
								"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
				} else
					this.sendHelpMessage(event, command, args);
				break;

			case "m":
			case "msg":
			case "message":
				if(args.length > 2) {
					NotificationInfo notificationInfo = guild.getNotification(NotificationType.TWITCH, args[1]);

					if(notificationInfo != null) {
						Document document = Document.parse(notificationInfo.message != null && !notificationInfo.message.isEmpty() ?
								notificationInfo.message :
								i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_TWITCH, locale));

						switch(args[2].toLowerCase()) {
						case "print":
							if(args.length > 3 && args[3].equalsIgnoreCase("json")) {
								this.sendMessage(event, MessageType.LIST, GsonUtil.GSON_PRETTY_PRINTING.toJson(document));
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

								String response = WebhookUtil.send(webhook.getUrl(), document.toJson()
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
										.replace("%urlg580x900", "https://wuffy.eu/pictures/example_box_art_url_320x180.png"));

								if(!response.isEmpty())
									this.sendMessage(event, MessageType.ERROR, "Response: " + response);

								if(deleteWebhook)
									webhook.delete().queue();
							}
							break;

						case "reset":
							guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_DEFAULT_EMBED_MESSAGE_TWITCH, locale));
							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_RESET, locale,
									"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
							break;

						case "rep":
						case "replace":
						case "replacement":
						case "replacements":
							this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_REPLACEMENTS_TWITCH, locale,
									"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
							break;

						case "c":
						case "copy":
							if(args.length > 3) {
								NotificationInfo notificationInfoFrom = guild.getNotification(NotificationType.TWITCH, args[3]);
								if(notificationInfoFrom != null) {
									this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_COPY, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
											"%f", notificationInfoFrom.name, //FROM
											"%t", notificationInfo.name)); //TO;
								} else
									this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_EXIST, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
							} else
								this.sendHelpMessage(event, command, args);
							break;

						case "s":
						case "set":
							if(args.length > 3) {
								String value = args.length > 4 ? args[4] : "";

								switch(args[3].toLowerCase()) {
								case "u":
								case "un":
								case "usern":
								case "username":
									if(value.matches("^[a-zA-Z0-9][\\w]{3,24}$")) {
										document.put("username", value);
										guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

										this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_USERNAME, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
												"%n", value));
									} else
										this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_URL, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
									break;

								case "a":
								case "avatar":
									if(value.length() < 100) {
										document.put("avatar_url", value);
										guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

										this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_AVATAR, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
												"%u", value));
									} else
										this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
									break;

								case "c":
								case "content":
									if(args.length > 4) {
										if(String.join(" ", Arrays.copyOfRange(args, 4, args.length)).length() < 100) {
											document.put("content", String.join(" ", Arrays.copyOfRange(args, 4, args.length)));
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_CONTENT, locale,
													"%c", String.join(" ", Arrays.copyOfRange(args, 4, args.length))));
										} else
											this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
									} else
										this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_CONTENT_NO_ARGS, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
									break;

								case "e":
								case "embed":
									if(args.length > 4) {
										value = args[5];
										Document embed = document.get("embeds", Arrays.asList(CommandTwitch.DEFAULT_EMBED_DOCUMENT)).get(0);

										switch (args[4].toLowerCase()) {
										case "title":
											if(String.join(" ", Arrays.copyOfRange(args, 5, args.length)).length() < 100) {
												embed.put("title", String.join(" ", Arrays.copyOfRange(args, 5, args.length)));
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%t", String.join(" ", Arrays.copyOfRange(args, 5, args.length))));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "desc":
										case "description":
											if(String.join(" ", Arrays.copyOfRange(args, 5, args.length)).length() < 100) {
												embed.put("description", String.join(" ", Arrays.copyOfRange(args, 5, args.length)));
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_DESCRIPTION, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%d", String.join(" ", Arrays.copyOfRange(args, 5, args.length))));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "url":
											if(value.length() < 100) {
												embed.put("url", value);
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_URL, locale,
														"%u", value));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "color":
											if(value.length() < 100) {
												String color = null;

												//Check RGB
												if(args.length > 6) {
													if(value.matches("[0-9]{1,3}") && args[6].matches("[0-9]{1,3}") && args[7].matches("[0-9]{1,3}") &&
															Integer.valueOf(value) < 256 && Integer.valueOf(args[6]) < 256 && Integer.valueOf(args[7]) < 256)
													color = this.convertColorToHex(new Color(Integer.valueOf(value), Integer.valueOf(args[6]), Integer.valueOf(args[7])));

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
														guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

														this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_COLOR, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
																"%c", color));
														break;
													}
												} catch(NumberFormatException ex) {
												}
												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_COLOR_NOT_FOUND, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "ficonurl":
										case "footericonurl":
											if(value.length() < 100) {
												if(!embed.containsKey("footer"))
													embed.put("footer", new Document("icon_url", value));
												else
													embed.get("footer", new Document()).put("icon_url", value);
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%u", value));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "footer":
											if(String.join(" ", Arrays.copyOfRange(args, 5, args.length)).length() < 100) {
												if(!embed.containsKey("footer"))
													embed.put("footer", new Document("text", String.join(" ", Arrays.copyOfRange(args, 5, args.length))));
												else
													embed.get("footer", new Document()).put("text", String.join(" ", Arrays.copyOfRange(args, 5, args.length)));
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%t", String.join(" ", Arrays.copyOfRange(args, 5, args.length))));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "thumbnail":
											if(value.length() < 100) {
												if(!embed.containsKey("thumbnail"))
													embed.put("thumbnail", new Document("url", value));
												else
													embed.get("thumbnail", new Document()).put("url", value);
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%u", value));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "image":
											if(value.length() < 100) {
												if(!embed.containsKey("image"))
													embed.put("image", new Document("url", value));
												else
													embed.get("image", new Document()).put("url", value);
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%u", value));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "authorname":
											if(String.join(" ", Arrays.copyOfRange(args, 5, args.length)).length() < 100) {
												if(!embed.containsKey("author"))
													embed.put("author", new Document("name", String.join(" ", Arrays.copyOfRange(args, 5, args.length))));
												else
													embed.get("author", new Document()).put("name", String.join(" ", Arrays.copyOfRange(args, 5, args.length)));
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%n", value));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "authorurl":
											if(value.length() < 100) {
												if(!embed.containsKey("author"))
													embed.put("author", new Document("url", value));
												else
													embed.get("author", new Document()).put("url", value);
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%u", value));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "authoriconurl":
											if(value.length() < 100) {
												if(!embed.containsKey("author"))
													embed.put("author", new Document("icon_url", value));
												else
													embed.get("author", new Document()).put("icon_url", value);
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
														"%u", value));
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "field":
										case "fields":
											if(value.length() < 100) {
												if(args.length > 7) {
													value = args[5];
													if(value.equalsIgnoreCase("add")) {
														if(embed.get("fields", new ArrayList<Document>()).size() < 6) {
															List<Document> fields = embed.get("fields", new ArrayList<Document>());
															fields.add(new Document("name", args[6])
																	.append("value", args[7])
																	.append("inline", Boolean.valueOf(args[8])));
															embed.put("fields", fields);
															guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

															this.sendMessage(event, MessageType.SUCCESS,
																	i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_TITLE, locale,
																			"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
																			"%n", args[6]));
														} else
															this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_FIELD_MAX_COUNT, locale,
																	"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
													} else if(value.equalsIgnoreCase("set")) {
														if(args.length > 8) {
															boolean found = false;

															for(Document field : embed.get("fields", new ArrayList<Document>())) {
																if(field.containsKey("name") && field.getString("name").equalsIgnoreCase(args[6])) {
																	List<Document> fields = embed.get("fields", new ArrayList<Document>());
																	fields.set(fields.indexOf(field), new Document("name", args[7])
																			.append("value", args[8])
																			.append("inline", Boolean.valueOf(args[9])));
																	embed.put("fields", fields);
																	guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

																	this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_SET_EMBED_FIELD_SET, locale,
																			"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
																			"%n", field.getString("name")));

																	found = true;
																	break;
																}
															}
															if(!found)
																this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_EMBED_FIELD_NOT_EXIST, locale,
																		"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
																		"%f", args[6]));
														} else
															this.sendHelpMessage(event, command, args);
													} else
														this.sendHelpMessage(event, command, args);
												} else
													this.sendHelpMessage(event, command, args);
											} else
												this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_TOO_LONG, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										default:
											this.sendHelpMessage(event, command, args);
											break;
										}
									} else
										this.sendHelpMessage(event, command, args);
									break;

								default:
									this.sendHelpMessage(event, command, args);
									break;
								}
							} else
								this.sendHelpMessage(event, command, args);
							break;

						case "r":
						case "remove":
						case "rem":
						case "uset":
						case "unset":
							switch(args[3].toLowerCase()) {
							case "avatar":
								document.remove("avatar_url");
								guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

								this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_TITLE, locale,
										"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
								break;

							case "content":
								if(document.containsKey("embeds")) {
									document.remove("content");
									guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

									this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_TITLE, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
								} else
									this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_REMOVE_CONTENT_OR_EMBED, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
								break;

							case "embed":
								Document embed = document.get("embeds", Arrays.asList(CommandTwitch.DEFAULT_EMBED_DOCUMENT)).get(0);
								if(document.containsKey("content")) {
									if(args.length < 5) {
										document.remove("embeds");
										guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.toJson());

										this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_TITLE, locale,
												"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
									} else
										switch(args[4].toLowerCase()) {
										case "title":
											embed.remove("title");
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_TITLE, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "description":
											embed.remove("description");
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_DESCRIPTION, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "url":
											embed.remove("url");
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_URL, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "color":
											embed.remove("color");
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_COLOR, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "footertext":
											embed.put("footer", embed.get("footer", new Document()).remove("text"));
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FOOTER_TEXT, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "ficonurl":
										case "footericonurl":
											embed.put("footer", embed.get("footer", new Document()).remove("icon_url"));
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FOOTER_ICON_URL, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "footer":
											embed.remove("footer");
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FOOTER, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "author":
											embed.remove("author");
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_AUTHOR, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "authorname":
											embed.put("author", embed.get("author", new Document()).remove("name"));
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_AUTHOR_NAME, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "authorurl":
											embed.put("author", embed.get("author", new Document()).remove("url"));
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_AUTHOR_URL, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "authoriconurl":
											embed.put("author", embed.get("author", new Document()).remove("icon_url"));
											guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_AUTHOR_ICON_URL, locale,
													"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											break;

										case "field":
										case "fields":
											if(args.length < 6) {
												embed.remove("fields");
												guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

												this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FIELD_REMOVE_ALL, locale,
														"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
											} else {
												boolean found = false;

												for(Document field : embed.get("fields", new ArrayList<Document>())) {
													if(field.containsKey("name") && field.getString("name").equalsIgnoreCase(args[5])) {
														List<Document> fields = embed.get("fields", new ArrayList<Document>());
														fields.remove(field);
														embed.put("fields", fields);
														guild.setNotificationMessage(NotificationType.TWITCH, notificationInfo.name, document.append("embeds", Arrays.asList(embed)).toJson());

														this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_UNSET_EMBED_FIELD_REMOVE, locale,
																"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
																"%n", field.getString("name")));

														found = true;
														break;
													}
												}

												if(!found)
													this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_EMBED_FIELD_NOT_EXIST, locale,
															"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale),
															"%f", args[5]));
											}
											break;

										default:
											this.sendHelpMessage(event, command, args);
											break;
										}
								} else
									this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_MESSAGE_REMOVE_CONTENT_OR_EMBED, locale,
											"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
								
								break;

							default:
								this.sendHelpMessage(event, command, args);
								break;
							}
							break;

						default:
							this.sendHelpMessage(event, command, args);
							break;
						}
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_NOT_EXIST, locale,
								"%type", i18n.format(TranslationKeys.MESSAGE_NOTIFICATION_TYPE_TWITCH, locale)));
				} else
					this.sendHelpMessage(event, command, args);
				break;

			default:
				this.sendHelpMessage(event, command, args);
				break;
			}
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }

	private String convertColorToHex(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
}