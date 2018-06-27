package de.ngloader.client;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.IWuffyGuild;
import de.ngloader.core.database.impl.IWuffyMemeber;
import de.ngloader.core.database.impl.IWuffyUser;

public class WuffyClient extends Core {

	static {
//		CommandRegistry.addCommand(AccountType.CLIENT, null);
	}

	public WuffyClient(ClientConfig config) {
		super(config);
	}

	@Override
	protected void onDisable() {
	}

	@Override
	protected void onUpdate() {
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