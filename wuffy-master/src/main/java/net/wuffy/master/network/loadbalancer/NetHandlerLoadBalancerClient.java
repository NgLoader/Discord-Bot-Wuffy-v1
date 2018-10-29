package net.wuffy.master.network.loadbalancer;

import javax.net.ssl.SSLException;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.WebUtil;
import net.wuffy.master.Master;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.loadbalancer.INetHandlerLoadBalancerClient;
import net.wuffy.network.loadbalancer.client.CPacketLoadBalancerMasterUpdate;
import net.wuffy.network.loadbalancer.server.SPacketLoadBalancerInit;
import net.wuffy.network.universal.INetHandlerUniversalClient;
import net.wuffy.network.universal.client.CPacketUniversalDisconnect;
import net.wuffy.network.universal.client.CPacketUniversalKeepAlive;
import net.wuffy.network.universal.server.SPacketUniversalKeepAlive;

public class NetHandlerLoadBalancerClient implements INetHandlerUniversalClient, INetHandlerLoadBalancerClient {

	private NetworkManager networkManager;

	public NetHandlerLoadBalancerClient(NetworkManager networkManager) {
		this.networkManager = networkManager;

		this.networkManager.sendPacket(new SPacketLoadBalancerInit(WebUtil.convertIpToInt(Master.getInstance().getConfig().masterAddress)));
	}

	@Override
	public void handleLoadBalancerMasterUpdate(CPacketLoadBalancerMasterUpdate loadBalancerMasterUpdate) {

		switch(loadBalancerMasterUpdate.getType()) {
			case START:
				Logger.info("LoadBalancer", "I'm now the master. (Starting Master)");
				try {
					Master.getInstance().getNetworkSystemMaster().start(Master.getInstance().getConfig());
					Logger.info("LoadBalancer", "Master started.");
				} catch (SSLException e) {
					Logger.fatal("Bootstrap", "SSLException", e);
				}
			break;

			case STOP:
				Logger.info("LoadBalancer", "No longer master. (Stopping Master)");
				Master.getInstance().getNetworkSystemMaster().getNetworkManagers().forEach(networkManager -> networkManager.close("Master stopping"));
			break;

			case RESTART:
				Logger.info("LoadBalancer", "Restarting Master.");

				Master.getInstance().getNetworkSystemMaster().getNetworkManagers().forEach(networkManager -> networkManager.close("Master stopping"));
				Logger.info("LoadBalancer", "Master stopped.");

				try {
					Master.getInstance().getNetworkSystemMaster().start(Master.getInstance().getConfig());
					Logger.info("LoadBalancer", "Master started.");
				} catch (SSLException e) {
					Logger.fatal("Bootstrap", "SSLException", e);
				}
			break;

			default:
				break;
		}
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