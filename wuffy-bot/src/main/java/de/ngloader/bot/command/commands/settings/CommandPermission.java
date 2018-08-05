package de.ngloader.bot.command.commands.settings;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyGuild.EnumPermissionMode;
import de.ngloader.bot.database.guild.WuffyGuild.EnumPermissionType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.database.impl.ImplMember;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.DiscordUtil;
import de.ngloader.core.util.StringUtil;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

@Command(aliases = { "permission", "permissions", "perm", "perms"})
@CommandConfig(category = CommandCategory.SETTINGS)
public class CommandPermission extends BotCommand {

	/*
	 * ~permission channel <channelId> <user|role|ranking> <id> <add|remove> <permissions>
	 * ~permission global <user|role|ranking> <id> <add|remove> <permissions>
	 * ~permission list channel <channelId> <user|role|ranking> <id>
	 * ~permission list global <user|role|ranking> <id>
	 * ~permission list <user|role|ranking> <id>
	 * ~permission list
	 * ~permission mode <add|remove> <mode>
	 */

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_PERMISSION)) {
			if(args.length > 0) {
				switch(args[0].toLowerCase()) {
				case "mode":
					if(args.length > 2) {
						EnumPermissionMode mode = EnumPermissionMode.search(args[2]);

						if(mode != null) {
							switch(args[1].toLowerCase()) {
							case "a":
							case "add":
								if(!guild.getPermissionMode().contains(mode)) {
									guild.addPermissionMode(mode);

									this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PERMISSION_MODE_ADDED, locale,
											"%n", StringUtil.writeFirstUpperCase(mode.name())));
								} else
									this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_MODE_ALREADY_ADDED, locale,
											"%n", StringUtil.writeFirstUpperCase(mode.name())));
								break;

							case "r":
							case "rem":
							case "remove":
								if(guild.getPermissionMode().contains(mode)) {
									guild.removePermissionMode(mode);

									this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PERMISSION_MODE_REMOVED, locale,
											"%n", StringUtil.writeFirstUpperCase(mode.name())));
								} else
									this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_MODE_ALREADY_REMOVED, locale,
											"%n", StringUtil.writeFirstUpperCase(mode.name())));
								break;

							default:
								this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
								break;
							}
						} else
							this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_MODE_NOT_FOUND, locale,
									"%n", args[2]));
					} else if(args[1].equalsIgnoreCase("list")) {
						this.replay(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_PERMISSION_LIST_MODE, locale,
								"%l", String.format("    - **%s**", String.join("**\n    - **", Arrays.asList(EnumPermissionMode.values()).stream()
										.map(mode -> StringUtil.writeFirstUpperCase(mode.name()))
										.collect(Collectors.toList())))));
					} else
						this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
					break;

				case "list":
					if(args.length > 2) {
						if(args[1].equalsIgnoreCase("global")) {
							EnumPermissionType type = EnumPermissionType.search(args[2]);
							if(type != null) {
								String id = null;
								String idDisplay = null;

								switch (type) {
								case RANKING:
									if(args[3].length() < 2) {
										id = idDisplay = args[3];
									}
									break;

								case ROLE:
									Role role = DiscordUtil.searchRole(guild, args[3]);

									if(role != null) {
										id = role.getId();
										idDisplay = role.getName();
									}
									break;

								case USER:
									ImplMember target = DiscordUtil.searchMember(event.getCore(), guild, args[3]);

									if(target != null) {
										id = target.getUser().getId();
										idDisplay = target.getEffectiveName();
									}
									break;

								default:
									break;
								}

								if(id != null) {
									List<String> permissions = guild.getPermissionGlobal(type, id);

									if(!permissions.isEmpty()) {
										this.replay(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_PERMISSION_LIST_GLOBAL, locale,
												"%t", StringUtil.writeFirstUpperCase(type.name()),
												"%id", idDisplay,
												"%l", String.format("    - **%s**", String.join("**\n    - **", permissions))));
									} else
										this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_EMPTY, locale,
												"%id", idDisplay));
								} else
									 this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_ID_NOT_FOUND, locale,
												"%n", args[2]));
							} else
								 this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_TYPE_NOT_FOUND, locale,
											"%n", args[1]));
						} else if(args[1].equalsIgnoreCase("channel")) {
							if(args.length > 3) {
								EnumPermissionType type = EnumPermissionType.search(args[3]);

								if(type != null) {
									String id = null;
									String idDisplay = null;

									switch (type) {
									case RANKING:
										if(args[4].length() < 2) {
											id = idDisplay = args[4];
										}
										break;

									case ROLE:
										Role role = DiscordUtil.searchRole(guild, args[4]);

										if(role != null) {
											id = role.getId();
											idDisplay = role.getName();
										}
										break;

									case USER:
										ImplMember target = DiscordUtil.searchMember(event.getCore(), guild, args[4]);

										if(target != null) {
											id = target.getUser().getId();
											idDisplay = target.getEffectiveName();
										}
										break;

									default:
										break;
									}

									TextChannel channel = DiscordUtil.searchChannel(guild, args[2]);

									if(id != null) {
										if(channel != null) {
											List<String> permissions = guild.getPermissionChannel(type, channel.getIdLong(), id);

											if(!permissions.isEmpty()) {
												this.replay(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_PERMISSION_LIST_CHANNEL, locale,
														"%t", StringUtil.writeFirstUpperCase(type.name()),
														"%id", idDisplay,
														"%c", channel.getName(),
														"%l", String.format("    - **%s**", String.join("**\n    - **", permissions))));
											} else
												this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_EMPTY, locale,
														"%id", idDisplay));
										} else this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_MODE_NOT_FOUND, locale,
												"%n", args[2]));
									} else
										 this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_ID_NOT_FOUND, locale,
													"%n", args[3]));
								} else
									 this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_TYPE_NOT_FOUND, locale,
												"%n", args[3]));
							} else
								this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
						} else
							this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
					} else if(args.length > 0) {
						 if(args[0].equalsIgnoreCase("list")) {
								List<String> keys =  Arrays.asList(PermissionKeys.values()).stream()
										.map(key -> StringUtil.writeFirstUpperCase(key.key))
										.collect(Collectors.toList());
								keys.addAll(Arrays.asList(CommandCategory.values()).stream().filter(category -> !category.key.isEmpty()).map(category -> category.key).collect(Collectors.toList()));

								this.replay(event, MessageType.LIST, i18n.format(TranslationKeys.MESSAGE_PERMISSION_LIST, locale,
										"%l", String.format("    - **%s**", String.join("**\n    - **", keys))));
							} else
								this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
					} else
						this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
					break;

				case "channel":
					if(args.length > 4) {
						TextChannel channel = DiscordUtil.searchChannel(guild, args[1]);

						if(channel != null) {
							EnumPermissionType type = EnumPermissionType.search(args[2]);

							if(type != null) {
								String id = null;
								String idDisplay = null;

								switch (type) {
								case RANKING:
									if(args[3].length() < 2) {
										id = idDisplay = args[3];
									}
									break;

								case ROLE:
									Role role = DiscordUtil.searchRole(guild, args[3]);

									if(role != null) {
										id = role.getId();
										idDisplay = role.getName();
									}
									break;

								case USER:
									ImplMember target = DiscordUtil.searchMember(event.getCore(), guild, args[3]);

									if(target != null) {
										id = target.getUser().getId();
										idDisplay = target.getEffectiveName();
									}
									break;

								default:
									break;
								}

								if(id != null) {
									String[] permissions = Arrays.copyOfRange(args, 5, args.length);
									List<String> has = guild.getPermissionChannel(type, channel.getIdLong(), id);

									if(args[4].equalsIgnoreCase("add") || args[4].equalsIgnoreCase("a")) {
										String[] add = Arrays.asList(permissions).stream()
												.filter(perm -> !has.contains(perm))
												.distinct()
												.toArray(String[]::new);

										if(add.length > 0) {
											guild.addPermissionChannel(type, channel.getIdLong(), id, add);

											this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_ADDED, locale,
													"%p", String.join(", ", add),
													"%id", idDisplay));
										} else
											this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_NOT_FOUND, locale,
												"%id", idDisplay));
									} else if(args[4].equalsIgnoreCase("remove") || args[4].equalsIgnoreCase("rem") || args[4].equalsIgnoreCase("r")) {
										String[] remove = Arrays.asList(permissions).stream()
												.filter(perm -> has.contains(perm))
												.distinct()
												.toArray(String[]::new);

										guild.removePermissionChannel(type, channel.getIdLong(), id, remove);

										this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_REMOVED, locale,
												"%p", String.join(", ", remove),
												"%id", idDisplay));
									} else
										this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
								} else
									this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_ID_NOT_FOUND, locale,
											"%n", args[3]));
							} else
								this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_TYPE_NOT_FOUND, locale,
										"%n", args[2]));
						} else
							this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_CHANNEL_NOT_FOUND, locale,
									"%n", args[1]));
					} else
						this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
					break;

				case "global":
					if(args.length > 3) {
						EnumPermissionType type = EnumPermissionType.search(args[1]);

						if(type != null) {
							String id = null;
							String idDisplay = null;

							switch (type) {
							case RANKING:
								if(args[2].length() < 2) {
									id = idDisplay = args[2];
								}
								break;

							case ROLE:
								Role role = DiscordUtil.searchRole(guild, args[2]);

								if(role != null) {
									id = role.getId();
									idDisplay = role.getName();
								}
								break;

							case USER:
								ImplMember target = DiscordUtil.searchMember(event.getCore(), guild, args[2]);

								if(target != null) {
									id = target.getUser().getId();
									idDisplay = target.getEffectiveName();
								}
								break;

							default:
								break;
							}

							if(id != null) {
								String[] permissions = Arrays.copyOfRange(args, 4, args.length);
								List<String> has = guild.getPermissionGlobal(type, id);

								if(args[3].equalsIgnoreCase("add") || args[3].equalsIgnoreCase("a")) {
									String[] add = Arrays.asList(permissions).stream()
											.filter(perm -> !has.contains(perm) && (PermissionKeys.search(perm) != null || CommandCategory.search(perm) != null))
											.distinct()
											.toArray(String[]::new);

									if(add.length > 0) {
										guild.addPermissionGlobal(type, id, add);

										this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_ADDED, locale,
												"%p", String.join(", ", add),
												"%id", idDisplay));
									} else
										this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_NOT_FOUND, locale,
											"%id", idDisplay));
								} else if(args[3].equalsIgnoreCase("remove") || args[3].equalsIgnoreCase("rem") || args[3].equalsIgnoreCase("r")) {
									String[] remove = Arrays.asList(permissions).stream()
											.filter(perm -> has.contains(perm))
											.distinct()
											.toArray(String[]::new);

									if(remove.length > 0) {
										guild.removePermissionGlobal(type, id, remove);

										this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_REMOVED, locale,
												"%p", String.join(", ", remove),
												"%id", idDisplay));
									} else
										this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_PERMISSION_PERM_NOT_FOUND, locale,
											"%id", idDisplay));
								} else
									this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
							} else
								this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_ID_NOT_FOUND, locale,
										"%n", args[3]));
						} else
							this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_PERMISSION_TYPE_NOT_FOUND, locale,
									"%n", args[2]));
					} else
						this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
					break;

				default:
					this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
					break;
				}
			} else
				this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_PERMISSION_SYNTAX, locale));
		} else
			this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_PERMISSION.key));
	}
}