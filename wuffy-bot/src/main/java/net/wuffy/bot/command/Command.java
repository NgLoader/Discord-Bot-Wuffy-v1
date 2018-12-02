package net.wuffy.bot.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.wuffy.bot.Wuffy;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.database.DBExtension;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.DBMember;
import net.wuffy.bot.database.DBUser;
import net.wuffy.core.lang.II18n;
import net.wuffy.core.util.ArgumentBuffer;

public abstract class Command {

	public abstract void onGuild(GuildMessageReceivedEvent event, DBGuild guild, DBMember member, String command, ArgumentBuffer args);
	public abstract void onPrivate(PrivateMessageReceivedEvent event, DBUser user, String command, ArgumentBuffer args);

	protected CommandSettings settings = this.getClass().getAnnotation(CommandSettings.class);

	public CommandSettings getSettings() {
		return this.settings;
	}

	public II18n getI18n() { //TODO find a better way to get i18n
		return Wuffy.getInstance().getStorageService().getStorage().getProvider(DBExtension.class).getI18n();
	}

	public String format(String key, String locale, String... params) {
		return this.getI18n().format(key, locale, params);
	}
}