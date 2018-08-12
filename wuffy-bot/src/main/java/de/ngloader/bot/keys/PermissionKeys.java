package de.ngloader.bot.keys;

import de.ngloader.bot.command.commands.CommandCategory;

public enum PermissionKeys {

	COMMAND_COMMANDS("command.commands", CommandCategory.INFORMATION),
	COMMAND_PING("command.ping", CommandCategory.INFORMATION),
	COMMAND_SHARDINFO("command.shardinfo", CommandCategory.INFORMATION),
	COMMAND_STATS("command.stats", CommandCategory.INFORMATION),
	COMMAND_STATUS("command.status", CommandCategory.INFORMATION),
	COMMAND_VERSION("command.version", CommandCategory.INFORMATION),

	COMMAND_BAN("command.ban", CommandCategory.ADMIN),
	COMMAND_CLEAR("command.clear", CommandCategory.ADMIN),
	COMMAND_HARDBAN("command.hardban", CommandCategory.ADMIN),
	COMMAND_KICK("command.kick", CommandCategory.ADMIN),
	COMMAND_MUTE("command.mute", CommandCategory.ADMIN),
	COMMAND_SOFTBAN("command.softban", CommandCategory.ADMIN),
	COMMAND_UNBAN("command.unban", CommandCategory.ADMIN),
	COMMAND_UNMUTE("command.unmute", CommandCategory.ADMIN),
	COMMAND_VCKICK("command.vckick", CommandCategory.ADMIN),
	COMMAND_ROLE("command.role", CommandCategory.ADMIN),

	COMMAND_AUTOPRUNE("command.autoprune", CommandCategory.SETTINGS),
	COMMAND_AUTOROLE("command.autorole", CommandCategory.SETTINGS),
	COMMAND_LANGUAGE_GUILD("command.language.guild", CommandCategory.SETTINGS),
	COMMAND_MENTION("command.mention", CommandCategory.SETTINGS),
	COMMAND_MESSAGE("command.message", CommandCategory.SETTINGS),
	COMMAND_PERMISSION("command.permission", CommandCategory.SETTINGS),
	COMMAND_PREFIX("command.prefix", CommandCategory.SETTINGS),
	COMMAND_RENAME("command.rename", CommandCategory.SETTINGS),
	COMMAND_NOTIFICATION("command.notification", CommandCategory.SETTINGS),
	COMMAND_NOTIFICATION_TWITCH("command.notification.twitch", CommandCategory.SETTINGS),
	COMMAND_NOTIFICATION_YOUTUBE("command.notification.youtube", CommandCategory.SETTINGS),

	COMMAND_GUILDINFO("command.guildinfo", CommandCategory.UTILITY),
	COMMAND_USERINFO("command.userinfo", CommandCategory.UTILITY),

	COMMAND_RULE34("command.role34", CommandCategory.NSFW),
	COMMAND_E621("command.e621", CommandCategory.NSFW),
	COMMAND_GTN("command.gtn", CommandCategory.NSFW),
	COMMAND_KONACHAN("command.konachan", CommandCategory.NSFW),
	COMMAND_YANDERE("command.yandere", CommandCategory.NSFW),

	COMMAND_CAT("command.cat", CommandCategory.IMAGE),
	COMMAND_DOG("command.dog", CommandCategory.IMAGE),
	COMMAND_GARFIELD("command.garfied", CommandCategory.IMAGE),
	COMMAND_CRY("command.cry", CommandCategory.IMAGE),
	COMMAND_CUDDLE("command.cuddle", CommandCategory.IMAGE),
	COMMAND_HUG("command.hug", CommandCategory.IMAGE),
	COMMAND_KISS("command.kiss", CommandCategory.IMAGE),
	COMMAND_LEWD("command.lewd", CommandCategory.IMAGE),
	COMMAND_LICK("command.lick", CommandCategory.IMAGE),
	COMMAND_NOM("command.nom", CommandCategory.IMAGE),
	COMMAND_NYAN("command.nyan", CommandCategory.IMAGE),
	COMMAND_OWO("command.owo", CommandCategory.IMAGE),
	COMMAND_PAT("command.pat", CommandCategory.IMAGE),
	COMMAND_POUT("command.pout", CommandCategory.IMAGE),
	COMMAND_REM("command.rem", CommandCategory.IMAGE),
	COMMAND_SLAP("command.slap", CommandCategory.IMAGE),
	COMMAND_SMUG("command.smug", CommandCategory.IMAGE),
	COMMAND_STARE("command.stare", CommandCategory.IMAGE),
	COMMAND_TICKLE("command.tickle", CommandCategory.IMAGE),
	COMMAND_TRIGGERED("command.triggered", CommandCategory.IMAGE),

	COMMAND_8BALL("command.8ball", CommandCategory.FUN),
	COMMAND_COINFLIP("command.coinflip", CommandCategory.FUN),
	COMMAND_TABLEFLIP("command.tableflip", CommandCategory.FUN),
	COMMAND_LOADING("command.loading", CommandCategory.FUN),
	COMMAND_ASCII("command.ascii", CommandCategory.FUN);

	public final String key;
	public final CommandCategory category;

	private PermissionKeys(String key, CommandCategory category) {
		this.key = key;
		this.category = category;
	}

	public static PermissionKeys search(String search) {
		for(PermissionKeys type : PermissionKeys.values())
			if(type.name().equalsIgnoreCase(search) || type.key.equalsIgnoreCase(search))
				return type;

		return null;
	}
}