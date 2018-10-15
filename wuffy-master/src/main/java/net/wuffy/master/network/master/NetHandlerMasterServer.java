package net.wuffy.master.network.master;

import java.util.UUID;

import net.wuffy.network.NetworkManager;
import net.wuffy.network.master.INetHandlerMasterServer;
import net.wuffy.network.master.server.SPacketMasterInit;
import net.wuffy.network.master.server.SPacketMasterStatsUpdate;
import net.wuffy.network.master.server.SPacketMasterStoppedShard;
import net.wuffy.network.master.server.SPacketMasterSystemUpdate;

public class NetHandlerMasterServer extends NetHandlerUniversalServer implements INetHandlerMasterServer {

	public NetHandlerMasterServer(NetworkManager networkManager, UUID uuid) {
		super(networkManager, uuid);
	}

	@Override
	public void handleMasterInit(SPacketMasterInit masterInit) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleMasterStatsUpdate(SPacketMasterStatsUpdate masterStatsUpdate) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleMasterSystemUpdate(SPacketMasterSystemUpdate masterSystemUpdate) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleMasterStoppedShard(SPacketMasterStoppedShard masterStoppedShard) {
		// TODO Auto-generated method stub
	}
}