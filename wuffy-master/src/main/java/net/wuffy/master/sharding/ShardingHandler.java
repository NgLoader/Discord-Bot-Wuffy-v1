package net.wuffy.master.sharding;

import java.util.List;
import java.util.stream.Collectors;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.ITickable;
import net.wuffy.master.GatewayBotInfo;
import net.wuffy.master.Master;
import net.wuffy.master.server.Server;
import net.wuffy.master.server.ServerHandler;
import net.wuffy.network.bot.client.CPacketBotShardUpdate;
import net.wuffy.network.bot.client.CPacketBotShardUpdate.EnumMasterShard;

public class ShardingHandler implements ITickable {

	private final ServerHandler serverHandler;

	private Long lastGatewayBotRefresh = 0L;
	private Long lastGatewayBotRefreshMessage = 0L;

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
		int needToRunByGuilds = (guilds / 2400) + 1; //I calculate 2400 guilds pro shard

		if(Master.getInstance().getConfig().minShardCount > needToRun) //I don't know why I added this, but I think it's useful
			needToRun = Master.getInstance().getConfig().minShardCount;

		if(needToRunByGuilds > needToRun) {
			needToRun = needToRunByGuilds;

			if(lastGatewayBotRefresh < System.currentTimeMillis()) {
				lastGatewayBotRefresh = System.currentTimeMillis() + 3600000;
				GatewayBotInfo.refresh();
			} else {
				if(this.lastGatewayBotRefreshMessage < System.currentTimeMillis()) {
					this.lastGatewayBotRefreshMessage = System.currentTimeMillis() + 960000; //Only send this message every 16 minutes
					Logger.debug("ServerHandler", String.format(
							"Discord sending lower chard count (%s) but master need (%s)",
							Integer.toString(GatewayBotInfo.getDiscord_shards().get()),
							Integer.toString(needToRun)));
				}

				GatewayBotInfo.getDiscord_shards().set(needToRun);
			}

			CPacketBotShardUpdate masterShardUpdatePacket = new CPacketBotShardUpdate(EnumMasterShard.SHARDCOUNT, needToRun);
			this.serverHandler.getServers().forEach(server -> server.getNetworkManager().sendPacket(masterShardUpdatePacket));
		}

		int currently = 0;

		do {
			int shardId = currently;
			if(this.serverHandler.getServers().stream().anyMatch(server -> server.isShardRunning(shardId)))
				continue;

			Server server = this.getBestSever();

			if(server != null)
				server.startShard(shardId);
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
		List<Server> valid = this.serverHandler.getServers().stream()
				.filter(server -> server.canShardStart()) //Filter is server ready
				.sorted((s1, s2) -> Integer.compare(s2.getShardsRunningCount(), s1.getShardsRunningCount())) //Sort by lowest shard count
				.collect(Collectors.toList()); //Collect to list

		return valid.isEmpty() ? null : valid.get(0);
	}
}