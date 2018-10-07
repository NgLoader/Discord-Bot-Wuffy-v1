package net.wuffy.network.loadbalancer.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.loadbalancer.INetHandlerLoadBalancerClient;

public class CPacketLoadBalancerNowMaster implements Packet<INetHandlerLoadBalancerClient> {

	public CPacketLoadBalancerNowMaster() { }

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
	}

	@Override
	public void handle(INetHandlerLoadBalancerClient handler) {
		handler.handleLoadBalancerNowMaster(this);
	}
}