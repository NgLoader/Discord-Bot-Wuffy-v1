package de.ngloader.bot.command.commands.settings;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.bot.util.ReplayBuilder;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.StringUtil;

@Command(aliases = { "message", "msg", "mess" })
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandMessage extends BotCommand {

	public static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

	public static final Map<String, Color> COLORS = new HashMap<String, Color>();

	static {
		CommandMessage.COLORS.put("white", Color.WHITE);
		CommandMessage.COLORS.put("lightgray", Color.LIGHT_GRAY);
		CommandMessage.COLORS.put("gray", Color.GRAY);
		CommandMessage.COLORS.put("darkgray", Color.DARK_GRAY);
		CommandMessage.COLORS.put("black", Color.BLACK);
		CommandMessage.COLORS.put("red", Color.RED);
		CommandMessage.COLORS.put("pink", Color.PINK);
		CommandMessage.COLORS.put("orange", Color.ORANGE);
		CommandMessage.COLORS.put("yellow", Color.YELLOW);
		CommandMessage.COLORS.put("green", Color.GREEN);
		CommandMessage.COLORS.put("magenta", Color.MAGENTA);
		CommandMessage.COLORS.put("cyan", Color.CYAN);
		CommandMessage.COLORS.put("blue", Color.BLUE);
	}

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_MESSAGE)) {
			if(args.length > 0) {
				switch (args[0].toLowerCase()) {
				case "info":
					new ReplayBuilder(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_MESSAGE_INFO, locale,
							"%e", i18n.format(String.format("message_%s", Boolean.toString(guild.isMessageDeleteExecuter()), locale), locale),
							"%b", i18n.format(String.format("message_%s", Boolean.toString(guild.isMessageDeleteBot()), locale), locale),
							"%d", guild.getMessageDeleteDelays().entrySet().stream()
								.map(entry -> String.format("**%s** - ``%ss.``", StringUtil.writeFirstUpperCase(entry.getKey().name()), Integer.toString(entry.getValue())))
								.collect(Collectors.joining("\n")),
							"%c", Arrays.asList(MessageType.values()).stream()
							.map(type -> String.format("**%s** - ``%s``", StringUtil.writeFirstUpperCase(type.name()),
									guild.getMessageColorCode(type) != null ?
										guild.getMessageColorCode(type) :
										this.convertColorToHex(type.color)))
							.collect(Collectors.joining("\n"))))
					.queue();
					break;

				case "executer":
					boolean setExecuter = !guild.isMessageDeleteExecuter();
					if(args.length > 1)
						setExecuter = Boolean.parseBoolean(args[1]);

					if(guild.isMessageDeleteExecuter() != setExecuter) {
						guild.setMessageDeleteExecuter(setExecuter);
						new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(setExecuter ?
								TranslationKeys.MESSAGE_MESSAGE_EXECUTER_ENABLE :
								TranslationKeys.MESSAGE_MESSAGE_EXECUTER_DISABLE, locale)).queue();
					} else
						new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(setExecuter ?
								TranslationKeys.MESSAGE_MESSAGE_ALREADY_ENABLED :
								TranslationKeys.MESSAGE_MESSAGE_ALREADY_DISABLED, locale)).queue();
					break;

				case "bot":
					boolean setBot = !guild.isMessageDeleteBot();
					if(args.length > 1)
						setBot = Boolean.parseBoolean(args[1]);

					if(guild.isMessageDeleteBot() != setBot) {
						guild.setMessageDeleteBot(setBot);
						new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(setBot ?
								TranslationKeys.MESSAGE_MESSAGE_BOT_ENABLE :
								TranslationKeys.MESSAGE_MESSAGE_BOT_DISABLE, locale)).queue();
					} else
						new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(setBot ?
								TranslationKeys.MESSAGE_MESSAGE_ALREADY_ENABLED :
								TranslationKeys.MESSAGE_MESSAGE_ALREADY_DISABLED, locale)).queue();
					break;

				case "types":
				case "type":
					new ReplayBuilder(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_LIST, locale,
							"%l", Arrays.asList(MessageType.values()).stream()
								.map(type -> StringUtil.writeFirstUpperCase(type.name()))
								.collect(Collectors.joining("\n"))))
					.queue();
					break;

				case "c":
				case "color":
					if(args.length > 1) {
						switch(args[1].toLowerCase()) {
						case "list":
							new ReplayBuilder(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_LIST, locale,
									"%l", String.format("    - **%s**", CommandMessage.COLORS.entrySet().stream(
											).map(entry -> StringUtil.writeFirstUpperCase(entry.getKey()))
											.collect(Collectors.joining("**\n    - **")))))
							.queue();
							break;

						case "info":
							new ReplayBuilder(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_INFO, locale,
									"%l", Arrays.asList(MessageType.values()).stream()
								.map(type -> String.format("**%s** - ``%s``", StringUtil.writeFirstUpperCase(type.name()),
										guild.getMessageColorCode(type) != null ?
											guild.getMessageColorCode(type) :
											this.convertColorToHex(type.color)))
								.collect(Collectors.joining("\n"))))
							.queue();
							break;

						case "set":
							if(args.length > 3) {
								MessageType messageType = null;

								for(MessageType type : MessageType.values())
									if(type.name().equalsIgnoreCase(args[2]) || type.name().startsWith(args[2])) {
										messageType = type;
										break;
									}

								if(messageType != null) {
									String color = null;

									//Check RGB
									if(args.length > 4) {
										if(args[3].matches("[0-9]{1,3}") && args[4].matches("[0-9]{1,3}") && args[5].matches("[0-9]{1,3}") &&
												Integer.valueOf(args[3]) < 256 && Integer.valueOf(args[4]) < 256 && Integer.valueOf(args[5]) < 256)
										color = this.convertColorToHex(new Color(Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5])));

									//Check default
									} else if(args[3].equalsIgnoreCase("default") || args[3].equalsIgnoreCase("d"))
										color = this.convertColorToHex(messageType.color);

									//Check hex
									else if(!(color = (args[3].startsWith("#") ? args[3] : String.format("#%s", args[3]))).matches(CommandMessage.HEX_PATTERN))
										color = null;

									//Check color names
									if(color == null)
										for(Map.Entry<String, Color> entry : CommandMessage.COLORS.entrySet())
											if(args[3].equalsIgnoreCase(entry.getKey())) {
												color = this.convertColorToHex(entry.getValue());
												break;
											}

									try {
										if(color != null && Color.decode(color) != null) {
											guild.setMessageColorCode(messageType, color);

											new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_SET, locale,
													"%t", StringUtil.writeFirstUpperCase(messageType.name()),
													"%v", color))
											.queue();
											break;
										}
									} catch(NumberFormatException ex) {
									}
									new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_NOT_FOUND, locale)).queue();
								} else
									new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_TYPE_NOT_FOUND, locale)).queue();
							} else
								new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_SYNTAX, locale)).queue();
							break;

							default:
								new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_SYNTAX, locale)).queue();
								break;
						}
					} else
						new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_SYNTAX, locale)).queue();
					break;

				case "d":
				case "delay":
					if(args.length > 1) {
						MessageType messageType = null;

						if(args.length > 2)
							for(MessageType type : MessageType.values())
								if(type.name().equalsIgnoreCase(args[2]) || type.name().startsWith(args[2])) {
									messageType = type;
									break;
								}

						switch(args[1].toLowerCase()) {

						case "r":
						case "rem":
						case "remove":
							if(args.length > 3) {
								if(args[3].matches("[0-9]{1,3}")) {
									Integer delay = Integer.parseInt(args[3]);

									if(delay > 0 && delay < 241) {
										if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("all")) {
											ReplayBuilder builder = new ReplayBuilder(event, MessageType.SUCCESS);

											for(MessageType type : MessageType.values()) {
												if(guild.isMessageDeleteDelay(type)) {
													guild.removeMessageDeleteDelay(type);
													builder.addDescription(i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_REMOVED, locale,
															"%t", StringUtil.writeFirstUpperCase(type.name())));
												} else
													builder.addDescription(i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ALREADY_REMOVED, locale,
															"%t", StringUtil.writeFirstUpperCase(type.name())));
											}

											builder.queue();
										}
									}
								}
							} else if(messageType != null) {
								if(guild.isMessageDeleteDelay(messageType)) {
									guild.removeMessageDeleteDelay(messageType);
									new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_REMOVED, locale,
											"%t", StringUtil.writeFirstUpperCase(messageType.name())))
									.queue();
								} else
									new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ALREADY_REMOVED, locale,
											"%t", StringUtil.writeFirstUpperCase(messageType.name())))
									.queue();
							} else
								new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_TYPE_NOT_FOUND, locale)).queue();
							break;

						case "s":
						case "set":
						case "a":
						case "add":
							if(args.length > 3) {
								if(args[3].matches("[0-9]{1,3}")) {
									Integer delay = Integer.parseInt(args[3]);

									if(delay > 0 && delay < 241) {
										if(args[2].equalsIgnoreCase("global") || args[2].equalsIgnoreCase("all")) {
											ReplayBuilder builder = new ReplayBuilder(event, MessageType.SUCCESS);

											for(MessageType type : MessageType.values()) {
												if(guild.isMessageDeleteDelay(type) && (guild.getMessageDeleteDelay(type) == delay))
													builder.addDescription("\n" + i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ALREADY_SET, locale));
												else {
													guild.setMessageDeleteDelay(type, delay);

													builder.addDescription("\n" + i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ADDED, locale,
															"%t", StringUtil.writeFirstUpperCase(type.name()),
															"%d", Integer.toString(delay)));
												}
											}

											builder.queue();
										} else if(messageType != null) {
											if(guild.isMessageDeleteDelay(messageType) && (guild.getMessageDeleteDelay(messageType) == delay))
												new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ALREADY_SET, locale)).queue();
											else {
												guild.setMessageDeleteDelay(messageType, delay);

												new ReplayBuilder(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ADDED, locale,
														"%t", StringUtil.writeFirstUpperCase(messageType.name()),
														"%d", Integer.toString(delay)))
												.queue();
											}
										} else
											new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_TYPE_NOT_FOUND, locale)).queue();
									} else
										new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOT_A_NUMBER, locale)).queue();
								} else
									new ReplayBuilder(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOT_A_NUMBER, locale)).queue();
							} else
								new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_SYNTAX, locale)).queue();
							break;

						case "l":
						case "list":
							new ReplayBuilder(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_LIST, locale,
									"%l", Arrays.asList(MessageType.values()).stream()
										.map(type -> StringUtil.writeFirstUpperCase(type.name()))
										.collect(Collectors.joining("\n"))))
							.queue();
							break;

						default:
							new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_SYNTAX, locale)).queue();
							break;
						}
					} else
						new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_SYNTAX, locale)).queue();
					break;

				default:
					new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_SYNTAX, locale)).queue();
					break;
				}
			} else
				new ReplayBuilder(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_SYNTAX, locale)).queue();
		} else
			new ReplayBuilder(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_MESSAGE.key)).queue();
	}

	private String convertColorToHex(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
}