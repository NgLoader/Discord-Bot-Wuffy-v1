package net.wuffy.master.network.master;

import java.util.UUID;

import net.wuffy.master.Master;
import net.wuffy.master.auth.AuthManager;
import net.wuffy.master.server.Server;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.bot.INetHandlerBotServer;
import net.wuffy.network.bot.server.SPacketBotHallo;
import net.wuffy.network.bot.server.SPacketBotStatsUpdate;
import net.wuffy.network.bot.server.SPacketBotStoppedShard;
import net.wuffy.network.bot.server.SPacketBotSystemUpdate;

public class NetHandlerMasterServer extends NetHandlerUniversalServer implements INetHandlerBotServer {

	private Server server;

	public NetHandlerMasterServer(NetworkManager networkManager, UUID uuid) {
		super(networkManager, uuid);

		this.server = new Server(this.networkManager, this.uuid, AuthManager.getName(this.uuid));
		Master.getInstance().getServerHandler().addServer(this.server);
	}

	@Override
	public void handleBotInit(SPacketBotHallo masterInit) {
		this.server.handlePacketHallo(masterInit);
	}

	@Override
	public void handleBotStatsUpdate(SPacketBotStatsUpdate masterStatsUpdate) {
		this.server.handlePacketStatsUpdate(masterStatsUpdate);
	}

	@Override
	public void handleBotSystemUpdate(SPacketBotSystemUpdate masterSystemUpdate) {
		this.server.handlePacketSystemUpdate(masterSystemUpdate);
	}

	@Override
	public void handleBotStoppedShard(SPacketBotStoppedShard masterStoppedShard) {
		this.server.stopShard(masterStoppedShard.getShardId());
	}

	@Override
	public void onDisconnect(String reason) {
		super.onDisconnect(reason);

		Master.getInstance().getServerHandler().removeServer(this.server);
	}
}