package net.wuffy.network.loadbalancer.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.loadbalancer.INetHandlerLoadBalancerServer;

public class SPacketLoadBalancerInit implements Packet<INetHandlerLoadBalancerServer> {

	private int addressAsInt;

	public SPacketLoadBalancerInit() { }

	public SPacketLoadBalancerInit(int addressAsInt) {
		this.addressAsInt = addressAsInt;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.addressAsInt = packetBuffer.readInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeInt(this.addressAsInt);
	}

	@Override
	public void handle(INetHandlerLoadBalancerServer handler) {
		handler.handleLoadBalancerInit(this);
	}

	public int getAddressAsInt() {
		return this.addressAsInt;
	}
}