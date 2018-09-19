package net.wuffy.master.sharding;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.ITickable;
import net.wuffy.master.GatewayBotInfo;

public class ServerHandler implements ITickable {

	private static final ServerHandler INSTANCE = new ServerHandler();

	public static ServerHandler getInstance() {
		return ServerHandler.INSTANCE;
	}

	private List<Server> servers = new ArrayList<Server>();

	private Long lastCheck = 0L;
	private Long lastNoFreeServerWarnMessage = 0L;

	public ServerHandler() {
		servers.add(new Server(UUID.randomUUID(), "Server #1", "0.0.0.0", 0, 0, 0, 0, 0, 100, 1));
	}

	@Override
	public void update() {
		if(lastCheck < System.currentTimeMillis())
			lastCheck = System.currentTimeMillis() + 5000;
		else
			return;

		int needToRun = GatewayBotInfo.getDiscord_shards().get();
		int currently = 0;

		do {
			int currentlyCopy = currently;
			if(this.servers.stream().anyMatch(server -> server.isShardRunning(currentlyCopy)))
				continue;

			Server server = this.getBestSever();

			if(server != null)
				server.startShard(currently);
			else {
				if(this.lastNoFreeServerWarnMessage < System.currentTimeMillis()) {
					this.lastNoFreeServerWarnMessage = System.currentTimeMillis() + 120000; //Only send this message every 2 minutes
					Logger.debug("ServerHandler", String.format("No free server for starting shard \"%s\".", currently));
				}
				break;
			}
		} while (++currently < needToRun);
	}

	public Server getBestSever() {
		//TODO calculate best server with lowest memory and loweds shards
		for(Server server : this.servers)
			if(server.canShardStart())
				return server;
		return null;
	}
}