package de.ngloader.bot.keys;

public enum PermissionKeys {

	COMMAND_COMMANDS("command.commands"),
	COMMAND_PING("command.ping"),
	COMMAND_SHARDINFO("command.shardinfo"),
	COMMAND_STATS("command.stats"),
	COMMAND_STATUS("command.status"),
	COMMAND_VERSION("command.version"),
	COMMAND_NOTIFICATION("command.notification"),
	COMMAND_NOTIFICATION_TWITCH("command.notification.twitch"),
	COMMAND_NOTIFICATION_YOUTUBE("command.notification.youtube"),

	COMMAND_BAN("command.ban"),
	COMMAND_CLEAR("command.clear"),
	COMMAND_HARDBAN("command.hardban"),
	COMMAND_KICK("command.kick"),
	COMMAND_MUTE("command.mute"),
	COMMAND_SOFTBAN("command.softban"),
	COMMAND_UNBAN("command.unban"),
	COMMAND_UNMUTE("command.unmute"),
	COMMAND_VCKICK("command.vckick"),

	COMMAND_AUTOPRUNE("command.autoprune"),
	COMMAND_AUTOROLE("command.autorole"),
	COMMAND_LANGUAGE_GUILD("command.language.guild"),
	COMMAND_MENTION("command.mention"),
	COMMAND_MESSAGE("command.message"),
	COMMAND_PERMISSION("command.permission"),
	COMMAND_PREFIX("command.prefix"),
	COMMAND_RENAME("command.rename"),

	COMMAND_GUILDINFO("command.guildinfo"),
	COMMAND_USERINFO("command.userinfo"),

	COMMAND_RULE34("command.role34"),
	COMMAND_E621("command.e621"),
	COMMAND_GTN("command.gtn"),
	COMMAND_KONACHAN("command.konachan"),
	COMMAND_YANDERE("command.yandere"),

	COMMAND_CAT("command.cat"),
	COMMAND_DOG("command.dog"),
	COMMAND_GARFIELD("command.garfied"),
	COMMAND_CRY("command.cry"),
	COMMAND_CUDDLE("command.cuddle"),
	COMMAND_HUG("command.hug"),
	COMMAND_KISS("command.kiss"),
	COMMAND_LEWD("command.lewd"),
	COMMAND_LICK("command.lick"),
	COMMAND_NOM("command.nom"),
	COMMAND_NYAN("command.nyan"),
	COMMAND_OWO("command.owo"),
	COMMAND_PAT("command.pat"),
	COMMAND_POUT("command.pout"),
	COMMAND_REM("command.rem"),
	COMMAND_SLAP("command.slap"),
	COMMAND_SMUG("command.smug"),
	COMMAND_STARE("command.stare"),
	COMMAND_TICKLE("command.tickle"),
	COMMAND_TRIGGERED("command.triggered"),

	COMMAND_8BALL("command.8ball"),
	COMMAND_COINFLIP("command.coinflip"),
	COMMAND_TABLEFLIP("command.tableflip"),
	COMMAND_LOADING("command.loading");

	public String key;
	public String storageKey;

	private PermissionKeys(String key) {
		this.key = key;
		this.storageKey = key.replace(".", "_");
	}
}