package net.wuffy.master.sharding;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.ITickable;
import net.wuffy.master.GatewayBotInfo;
import net.wuffy.master.server.Server;
import net.wuffy.master.server.ServerHandler;

public class ShardingHandler implements ITickable {

	private final ServerHandler serverHandler;

	private Long lastCheck = 0L;
	private Long lastNoFreeServerWarnMessage = 0L;

	public ShardingHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

	@Override
	public void update() {
		if(lastCheck < System.currentTimeMillis())
			lastCheck = System.currentTimeMillis() + 5000;
		else
			return;

		int guilds = 0;
		for(Server server : this.serverHandler.getServers())
			guilds += server.isReady() ? server.getStatsUpdate().getGuildCount() : 0;

		int needToRun = GatewayBotInfo.getDiscord_shards().get();
		int needToRunByGuilds = (guilds / 2500) + 1;

		if(needToRunByGuilds > needToRun)
			GatewayBotInfo.getDiscord_shards().set(needToRun = needToRunByGuilds);

		int currently = 0;

		do {
			int currentlyCopy = currently;
			if(this.serverHandler.getServers().stream().anyMatch(server -> server.isShardRunning(currentlyCopy)))
				continue;

			Server server = this.getBestSever();

			if(server != null)
				server.startShard(currently);
			else {
				if(this.lastNoFreeServerWarnMessage < System.currentTimeMillis()) {
					this.lastNoFreeServerWarnMessage = System.currentTimeMillis() + 960000; //Only send this message every 16 minutes
					Logger.debug("ServerHandler", String.format("No free server for starting shard \"%s\".", currently));
				}
				break;
			}
		} while (++currently < needToRun);
	}

	public Server getBestSever() {
		//TODO calculate best server with lowest memory and loweds shards
		for(Server server : this.serverHandler.getServers())
			if(server.canShardStart())
				return server;
		return null;
	}
}