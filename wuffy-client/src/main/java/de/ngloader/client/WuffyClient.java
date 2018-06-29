package de.ngloader.client;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.IWuffyGuild;
import de.ngloader.core.database.impl.IWuffyMemeber;
import de.ngloader.core.database.impl.IWuffyUser;
import net.dv8tion.jda.core.AccountType;

public class WuffyClient extends Core {

	static {
		//TODO Add commands
	}

	public WuffyClient(ClientConfig config) {
		super(config, AccountType.CLIENT);
	}

	@Override
	protected void onEnable() {	
	}

	@Override
	protected void onDisable() {
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