package net.wuffy.bot.command.commands.settings;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.EmbedBuilder;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.common.util.StringUtil;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;

@CommandSettings(
		category = CommandCategory.SETTINGS,
		memberPermissionList = { PermissionKeys.COMMAND_MESSAGE },
		memberPermissionRequierd = { PermissionKeys.COMMAND_MESSAGE },
		aliases = { "message", "msg" })
public class CommandMessage extends Command {

	public CommandMessage(CommandHandler handler) {
		super(handler);
	}

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		String locale = member.getLocale();

		if(args.getSize() > 0) {
			switch (args.get(0).toLowerCase()) {
			case "info":
				this.sendMessage(event, MessageType.INFO, i18n.format(TranslationKeys.MESSAGE_MESSAGE_INFO, locale,
						"%e", i18n.format(String.format("message_%s", Boolean.toString(guild.isMessageDeleteExecuter()), locale), locale),
						"%b", i18n.format(String.format("message_%s", Boolean.toString(guild.isMessageDeleteBot()), locale), locale),
						"%d", guild.getMessageDeleteDelays().entrySet().stream()
							.map(entry -> String.format("**%s** - ``%ss.``", StringUtil.writeFirstUpperCase(entry.getKey().name()), Integer.toString(entry.getValue())))
							.collect(Collectors.joining("\n")),
						"%c", Arrays.asList(MessageType.values()).stream()
						.filter(type -> type != MessageType.LOADING)
						.map(type -> String.format("**%s** - ``%s``", StringUtil.writeFirstUpperCase(type.name()),
								guild.getMessageColorCode(type) != null ?
									guild.getMessageColorCode(type) :
									this.convertColorToHex(type.color)))
						.collect(Collectors.joining("\n"))));
				break;

			case "executer":
				boolean setExecuter = !guild.isMessageDeleteExecuter();
				if(args.getSize() > 1)
					setExecuter = Boolean.parseBoolean(args.get(1));

				if(guild.isMessageDeleteExecuter() != setExecuter) {
					guild.setMessageDeleteExecuter(setExecuter);
					this.sendMessage(event, MessageType.SUCCESS, i18n.format(setExecuter ?
							TranslationKeys.MESSAGE_MESSAGE_EXECUTER_ENABLE :
							TranslationKeys.MESSAGE_MESSAGE_EXECUTER_DISABLE, locale));
				} else
					this.sendMessage(event, MessageType.SUCCESS, i18n.format(setExecuter ?
							TranslationKeys.MESSAGE_MESSAGE_ALREADY_ENABLED :
							TranslationKeys.MESSAGE_MESSAGE_ALREADY_DISABLED, locale));
				break;

			case "bot":
				boolean setBot = !guild.isMessageDeleteBot();
				if(args.getSize() > 1)
					setBot = Boolean.parseBoolean(args.get(1));

				if(guild.isMessageDeleteBot() != setBot) {
					guild.setMessageDeleteBot(setBot);
					this.sendMessage(event, MessageType.SUCCESS, i18n.format(setBot ?
							TranslationKeys.MESSAGE_MESSAGE_BOT_ENABLE :
							TranslationKeys.MESSAGE_MESSAGE_BOT_DISABLE, locale));
				} else
					this.sendMessage(event, MessageType.SUCCESS, i18n.format(setBot ?
							TranslationKeys.MESSAGE_MESSAGE_ALREADY_ENABLED :
							TranslationKeys.MESSAGE_MESSAGE_ALREADY_DISABLED, locale));
				break;

			case "types":
			case "type":
				this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_LIST, locale,
						"%l", Arrays.asList(MessageType.values()).stream()
							.filter(type -> type != MessageType.LOADING)
							.map(type -> StringUtil.writeFirstUpperCase(type.name()))
							.collect(Collectors.joining("\n"))));
				break;

			case "c":
			case "color":
				if(args.getSize() > 1) {
					switch(args.get(1).toLowerCase()) {
					case "list":
						this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_LIST, locale,
								"%l", String.format("    - **%s**", CommandMessage.COLORS.entrySet().stream(
										).map(entry -> StringUtil.writeFirstUpperCase(entry.getKey()))
										.collect(Collectors.joining("**\n    - **")))));
						break;

					case "info":
						this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_INFO, locale,
								"%l", Arrays.asList(MessageType.values()).stream()
									.filter(type -> type != MessageType.LOADING)
									.map(type -> String.format("**%s** - ``%s``", StringUtil.writeFirstUpperCase(type.name()),
											guild.getMessageColorCode(type) != null ?
													guild.getMessageColorCode(type) :
													this.convertColorToHex(type.color)))
							.collect(Collectors.joining("\n"))));
						break;

					case "set":
						if(args.getSize() > 3) {
							MessageType messageType = null;

							for(MessageType type : MessageType.values())
								if(type.name().equalsIgnoreCase(args.get(2)) || type.name().startsWith(args.get(2))) {
									messageType = type;
									break;
								}

							if(messageType != null) {
								String color = null;

								//Check RGB
								if(args.getSize() > 4) {
									if(args.get(3).matches("[0-9]{1,3}") && args.get(4).matches("[0-9]{1,3}") && args.get(5).matches("[0-9]{1,3}") &&
											Integer.valueOf(args.get(3)) < 256 && Integer.valueOf(args.get(4)) < 256 && Integer.valueOf(args.get(5)) < 256)
									color = this.convertColorToHex(new Color(Integer.valueOf(args.get(3)), Integer.valueOf(args.get(4)), Integer.valueOf(args.get(5))));

								//Check default
								} else if(args.get(3).equalsIgnoreCase("default") || args.get(3).equalsIgnoreCase("d"))
									color = this.convertColorToHex(messageType.color);

								//Check hex
								else if(!(color = (args.get(3).startsWith("#") ? args.get(3) : String.format("#%s", args.get(3)))).matches(CommandMessage.HEX_PATTERN))
									color = null;

								//Check color names
								if(color == null)
									for(Map.Entry<String, Color> entry : CommandMessage.COLORS.entrySet())
										if(args.get(3).equalsIgnoreCase(entry.getKey())) {
											color = this.convertColorToHex(entry.getValue());
											break;
										}

								try {
									if(color != null && Color.decode(color) != null) {
										guild.setMessageColorCode(messageType, color);

										this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_SET, locale,
												"%t", StringUtil.writeFirstUpperCase(messageType.name()),
												"%v", color));
										break;
									}
								} catch(NumberFormatException ex) {
								}
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_NOT_FOUND, locale));
							} else
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_COLOR_TYPE_NOT_FOUND, locale));
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

			case "d":
			case "delay":
				if(args.getSize() > 1) {
					MessageType messageType = null;

					if(args.getSize() > 2)
						for(MessageType type : MessageType.values())
							if(type != MessageType.LOADING && (type.name().equalsIgnoreCase(args.get(2)) || type.name().startsWith(args.get(2)))) {
								messageType = type;
								break;
							}

					switch(args.get(1).toLowerCase()) {

					case "r":
					case "rem":
					case "remove":
						if(args.getSize() > 3) {
							if(args.get(3).matches("[0-9]{1,3}")) {
								Integer delay = Integer.parseInt(args.get(3));

								if(delay > 0 && delay < 241) {
									if(args.get(2).equalsIgnoreCase("global") || args.get(2).equalsIgnoreCase("all")) {
										EmbedBuilder builder = this.createEmbed(event, MessageType.SUCCESS);

										for(MessageType type : MessageType.values()) {
											if(type != MessageType.LOADING)
												if(guild.isMessageDeleteDelay(type)) {
													guild.removeMessageDeleteDelay(type);
													builder.appendDescription(i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_REMOVED, locale,
															"%t", StringUtil.writeFirstUpperCase(type.name())));
												} else
													builder.appendDescription(i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ALREADY_REMOVED, locale,
															"%t", StringUtil.writeFirstUpperCase(type.name())));
										}

										this.queue(event, MessageType.SUCCESS, event.getTextChannel().sendMessage(builder.build()));
									}
								}
							}
						} else if(messageType != null) {
							if(guild.isMessageDeleteDelay(messageType)) {
								guild.removeMessageDeleteDelay(messageType);
								this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_REMOVED, locale,
										"%t", StringUtil.writeFirstUpperCase(messageType.name())));
							} else
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ALREADY_REMOVED, locale,
										"%t", StringUtil.writeFirstUpperCase(messageType.name())));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_TYPE_NOT_FOUND, locale));
						break;

					case "s":
					case "set":
					case "a":
					case "add":
						if(args.getSize() > 3) {
							if(args.get(3).matches("[0-9]{1,3}")) {
								Integer delay = Integer.parseInt(args.get(3));

								if(delay > 0 && delay < 241) {
									if(args.get(2).equalsIgnoreCase("global") || args.get(2).equalsIgnoreCase("all")) {
										EmbedBuilder builder = this.createEmbed(event, MessageType.SUCCESS);

										for(MessageType type : MessageType.values()) {
											if(type != MessageType.LOADING)
												if(guild.isMessageDeleteDelay(type) && (guild.getMessageDeleteDelay(type) == delay))
													builder.appendDescription("\n" + i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ALREADY_SET, locale));
												else {
													guild.setMessageDeleteDelay(type, delay);
	
													builder.appendDescription("\n" + i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ADDED, locale,
															"%t", StringUtil.writeFirstUpperCase(type.name()),
															"%d", Integer.toString(delay)));
												}
										}

										this.queue(event, MessageType.SUCCESS, event.getTextChannel().sendMessage(builder.build()));
									} else if(messageType != null) {
										if(guild.isMessageDeleteDelay(messageType) && (guild.getMessageDeleteDelay(messageType) == delay))
											this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ALREADY_SET, locale));
										else {
											guild.setMessageDeleteDelay(messageType, delay);

											this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_ADDED, locale,
													"%t", StringUtil.writeFirstUpperCase(messageType.name()),
													"%d", Integer.toString(delay)));
										}
									} else
										this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_TYPE_NOT_FOUND, locale));
								} else
									this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOT_A_NUMBER, locale));
							} else
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOT_A_NUMBER, locale));
						} else
							this.sendHelpMessage(event, command, args);
						break;

					case "l":
					case "list":
						this.sendMessage(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_MESSAGE_DELAY_LIST, locale,
								"%l", Arrays.asList(MessageType.values()).stream()
									.filter(type -> type != MessageType.LOADING)
									.map(type -> StringUtil.writeFirstUpperCase(type.name()))
									.collect(Collectors.joining("\n"))));
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
	}

	private String convertColorToHex(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}