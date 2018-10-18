package net.wuffy.bot.jda.sharding;

import java.util.concurrent.atomic.AtomicLong;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.tuple.Pair;
import net.wuffy.common.logger.Logger;
import net.wuffy.network.master.client.CPacketMasterSettings;

public class WuffySessionController implements SessionController {

	private CPacketMasterSettings gatewayBot;
	private Pair<String, Integer> gatewayBotPair;

	private AtomicLong globalRatelimit = new AtomicLong(Long.MIN_VALUE);

	public WuffySessionController(CPacketMasterSettings gatewayBot) {
		this.gatewayBot = gatewayBot;
		this.gatewayBotPair = Pair.of(this.gatewayBot.getUrl(), this.gatewayBot.getShards());
	}

	public void handlePacketMasterGatewayBot(CPacketMasterSettings gatewayBot) {
		this.gatewayBot = gatewayBot;
		this.gatewayBotPair = Pair.of(this.gatewayBot.getUrl(), this.gatewayBot.getShards());
	}

	@Override
	public void appendSession(SessionConnectNode node) {
		try {
			node.run(true);
		} catch (InterruptedException e) {
			Logger.fatal("WuffySessionController", "Error by append session", e);
		}
	}

	@Override
	public void removeSession(SessionConnectNode node) {
		//Currently not needed
	}

	@Override
	public long getGlobalRatelimit() {
		return this.globalRatelimit.get();
	}

	@Override
	public void setGlobalRatelimit(long ratelimit) {
		this.globalRatelimit.set(ratelimit);
	}

	@Override
	public String getGateway(JDA api) {
		return this.gatewayBot.getUrl();
	}

	@Override
	public Pair<String, Integer> getGatewayBot(JDA api) {
		return this.gatewayBotPair;
	}
}