package de.ngloader.bot;

import de.ngloader.core.Core;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.database.impl.IWuffyGuild;
import de.ngloader.core.database.impl.IWuffyMemeber;
import de.ngloader.core.database.impl.IWuffyUser;
import net.dv8tion.jda.core.AccountType;

public class WuffyBot extends Core {

	static {
		//TODO Add commands
	}

	private CommandManager<WuffyBot> commandManager;

	public WuffyBot(BotConfig config) {
		super(config, AccountType.BOT);

		this.commandManager = new de.ngloader.bot.command.CommandManager(this);
	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDisable() {
	}

	public CommandManager<WuffyBot> getCommandManager() {
		return commandManager;
	}

	@Override
	public IWuffyGuild getGuild(long guildId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWuffyUser getUser(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWuffyMemeber getMember(long guildId, long memberId) {
		// TODO Auto-generated method stub
		return null;
	}
}