package net.wuffy.bot.jda.sharding;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.tuple.Pair;

public class WuffySessionController implements SessionController {

	public WuffySessionController() {
	}

	@Override
	public void appendSession(SessionConnectNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeSession(SessionConnectNode node) {
		// TODO Auto-generated method stub
	}

	@Override
	public long getGlobalRatelimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setGlobalRatelimit(long ratelimit) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getGateway(JDA api) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<String, Integer> getGatewayBot(JDA api) {
		// TODO Auto-generated method stub
		return null;
	}
}