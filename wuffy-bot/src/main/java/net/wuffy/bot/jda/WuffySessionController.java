package net.wuffy.bot.jda;

import java.util.concurrent.atomic.AtomicLong;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.tuple.Pair;
import net.wuffy.common.logger.Logger;
import net.wuffy.network.bot.client.CPacketBotSettings.GatewayBot;

public class WuffySessionController implements SessionController {

	private GatewayBot gatewayBot;

	private Pair<String, Integer> urlWithShards;
	private AtomicLong globalRateLimit = new AtomicLong(Long.MIN_VALUE);

	public WuffySessionController(GatewayBot gatewayBot) {
		this.handleGateWayBotUpdate(gatewayBot);
	}

	public void handleGateWayBotUpdate(GatewayBot gatewayBot) {
		this.gatewayBot = gatewayBot;

		this.urlWithShards = Pair.of(this.gatewayBot.getUrl(), this.gatewayBot.getShards());
	}

	@Override
	public void appendSession(SessionConnectNode node) {
		try {
			node.run(false);

			Logger.info("SessionController", String.format("Starting session \"%s\"", node.getShardInfo().getShardString()));
		} catch (InterruptedException e) {
			Logger.fatal("SessionController", String.format("Error starting session \"%s\"", node != null && node.getShardInfo() != null ? node.getShardInfo().getShardString() : "UNKNOWN"), e);
		}
	}

	@Override
	public void removeSession(SessionConnectNode node) {
		Logger.info("SessionController", String.format("Destroying session \"%s\"", node.getShardInfo().getShardString()));
	}

	@Override
	public long getGlobalRatelimit() {
		return this.globalRateLimit.get();
	}

	@Override
	public void setGlobalRatelimit(long ratelimit) {
		this.globalRateLimit.set(ratelimit);
	}

	@Override
	public String getGateway(JDA api) {
		return this.gatewayBot.getUrl();
	}

	@Override
	public Pair<String, Integer> getGatewayBot(JDA api) {
		return this.urlWithShards;
	}
}