package net.wuffy.master.network.loadbalancer;

import net.wuffy.common.logger.Logger;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.loadbalancer.INetHandlerLoadBalancerClient;
import net.wuffy.network.loadbalancer.client.CPacketLoadBalancerNowMaster;
import net.wuffy.network.universal.INetHandlerUniversalClient;
import net.wuffy.network.universal.client.CPacketUniversalDisconnect;
import net.wuffy.network.universal.client.CPacketUniversalKeepAlive;
import net.wuffy.network.universal.server.SPacketUniversalKeepAlive;

public class NetHandlerLoadBalancerClient implements INetHandlerUniversalClient, INetHandlerLoadBalancerClient {

	private NetworkManager networkManager;

	public NetHandlerLoadBalancerClient(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	public void handleLoadBalancerNowMaster(CPacketLoadBalancerNowMaster loadBalancerNowMaster) {
		Logger.info("LoadBalancer", "I'm now the master.");

		//TODO start master netty server for client to connect
	}

	@Override
	public void handleKeepAlive(CPacketUniversalKeepAlive keepAlive) {
		this.networkManager.sendPacket(new SPacketUniversalKeepAlive(keepAlive.getTimestamp()));
	}

	@Override
	public void handleDisconnect(CPacketUniversalDisconnect disconnect) {
		this.onDisconnect(disconnect.getReason());
	}

	@Override
	public void onDisconnect(String reason) {
		Logger.info(String.format("Disconnected: %s", reason));
	}
}