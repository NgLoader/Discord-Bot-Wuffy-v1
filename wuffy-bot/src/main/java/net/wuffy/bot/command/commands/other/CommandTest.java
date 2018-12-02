package net.wuffy.bot.command.commands.other;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.wuffy.bot.command.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.database.DBGuild;
import net.wuffy.bot.database.DBMember;
import net.wuffy.bot.database.DBUser;
import net.wuffy.core.util.ArgumentBuffer;

@CommandSettings(
		aliases = { "test", "t" },
		category = CommandCategory.OTHER,
		memberPermissionList = { },
		admin = true,
		hidden = true,
		privateChat = true)
public class CommandTest extends Command {

	@Override
	public void onGuild(GuildMessageReceivedEvent event, DBGuild guild, DBMember member, String command, ArgumentBuffer args) {
		event.getChannel().sendMessage("Tested!").queue();
	}

	@Override
	public void onPrivate(PrivateMessageReceivedEvent event, DBUser user, String command, ArgumentBuffer args) {
		event.getChannel().sendMessage("Tested!").queue();
	}
}