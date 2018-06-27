package de.ngloader.bot;

import de.ngloader.bot.command.CommandExecutor;
import de.ngloader.bot.command.CommandTrigger;
import de.ngloader.core.Core;
import de.ngloader.core.command.CommandManager;
import de.ngloader.core.database.impl.IWuffyGuild;
import de.ngloader.core.database.impl.IWuffyMemeber;
import de.ngloader.core.database.impl.IWuffyUser;

public class WuffyBot extends Core {

	static {
//		CommandRegistry.addCommand(AccountType.BOT, null);
	}

	private CommandManager<WuffyBot> commandManager;

	public WuffyBot(BotConfig config) {
		super(config);

		this.commandManager = new CommandManager<WuffyBot>(this, new CommandExecutor(this.commandManager), new CommandTrigger(this.commandManager));
	}

	@Override
	protected void onLoad() {
	}

	@Override
	protected void onDisable() {
	}

	@Override
	protected void onUpdate() {
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