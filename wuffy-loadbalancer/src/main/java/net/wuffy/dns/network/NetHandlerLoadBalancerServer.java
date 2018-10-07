package net.wuffy.dns.network;

import java.util.UUID;

import net.wuffy.network.NetworkManager;
import net.wuffy.network.loadbalancer.INetHandlerLoadBalancerServer;
import net.wuffy.network.loadbalancer.server.SPacketLoadBalancerInit;

public class NetHandlerLoadBalancerServer extends NetHandlerUniversalServer implements INetHandlerLoadBalancerServer {

	private final NetworkManager networkManager;
	private UUID uuid;

	private boolean init = true;

	private int addressAsInt;

	public NetHandlerLoadBalancerServer(NetworkManager networkManager, UUID uuid) {
		super(networkManager, uuid);

		this.networkManager = networkManager;
		this.uuid = uuid;
	}

	@Override
	public void handleLoadBalancerInit(SPacketLoadBalancerInit loadBalancerInit) {
		this.addressAsInt = loadBalancerInit.getAddressAsInt();

		this.init = false;
	}

	@Override
	public void onDisconnect(String reason) {
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public int getAddressAsInt() {
		return this.addressAsInt;
	}

	public boolean isInit() {
		return this.init;
	}
}