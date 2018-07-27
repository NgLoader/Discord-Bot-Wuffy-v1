package de.ngloader.bot.keys;

public enum PermissionKeys {

	COMMAND_COMMANDS("command.commands"),
	COMMAND_PING("command.ping"),
	COMMAND_SHARDINFO("command.shardinfo"),
	COMMAND_STATS("command.stats"),
	COMMAND_STATUS("command.status"),
	COMMAND_VERSION("command.version"),
	COMMAND_TWTICH("command.twitch"),

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

	COMMAND_USERINFO("command.userinfo");

	public String key;
	public String storageKey;

	private PermissionKeys(String key) {
		this.key = key;
		this.storageKey = key.replace(".", "_");
	}
}