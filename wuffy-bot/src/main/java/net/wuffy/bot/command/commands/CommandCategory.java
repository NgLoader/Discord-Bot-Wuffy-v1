package net.wuffy.bot.command.commands;

public enum CommandCategory {

	BOT_AUTHOR(""),
	ADMIN("category.admin"),
	SETTINGS("category.settings"),
	INFORMATION("category.information"),
	MUSIC("category.music"),
	NSFW("category.nsfw"),
	UTILITY("category.utility"),
	OTHER("category.other"),
	FUN("category.fun"),
	IMAGE("category.image"),
	GAMES("category.games"),
	PERMS("category.perms");

	public final String key;

	private CommandCategory(String key) {
		this.key = key;
	}

	public static CommandCategory search(String search) {
		for(CommandCategory type : CommandCategory.values())
			if(type.name().equalsIgnoreCase(search) || type.key.equalsIgnoreCase(search))
				return type;

		return null;
	}
}