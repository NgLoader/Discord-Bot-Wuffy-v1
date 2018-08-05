package de.ngloader.bot.command;

public enum CommandCategory {

	BOT_AUTHOR(""),
	MODERATOR("category.moderator"),
	SETTINGS("category.settings"),
	INFORMATION("category.informtation"),
	MUSIC("category.music"),
	NSFW("category.nsfw"),
	UTILITY("category.utility"),
	OTHER("category.other"),
	FUN("category.fun"),
	IMAGE("category.image");

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