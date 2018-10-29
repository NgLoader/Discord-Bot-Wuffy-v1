package net.wuffy.network.loadbalancer.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.loadbalancer.INetHandlerLoadBalancerClient;

public class CPacketLoadBalancerMasterUpdate implements Packet<INetHandlerLoadBalancerClient> {

	private EnumLoadBalancerMasterUpdate type;

	public CPacketLoadBalancerMasterUpdate() { }

	public CPacketLoadBalancerMasterUpdate(EnumLoadBalancerMasterUpdate type) {
		this.type = type;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.type = packetBuffer.readEnum(EnumLoadBalancerMasterUpdate.class);
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeEnum(this.type);
	}

	@Override
	public void handle(INetHandlerLoadBalancerClient handler) {
		handler.handleLoadBalancerMasterUpdate(this);
	}

	public EnumLoadBalancerMasterUpdate getType() {
		return this.type;
	}

	public enum EnumLoadBalancerMasterUpdate {
		START, STOP, RESTART
	}
}