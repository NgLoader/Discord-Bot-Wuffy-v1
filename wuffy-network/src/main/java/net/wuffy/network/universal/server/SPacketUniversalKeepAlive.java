package net.wuffy.network.universal.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.universal.INetHandlerUniversalServer;

public class SPacketUniversalKeepAlive implements Packet<INetHandlerUniversalServer> {

	private long timestamp;

	public SPacketUniversalKeepAlive() { }

	public SPacketUniversalKeepAlive(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.timestamp = packetBuffer.readVarLong();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeVarLong(this.timestamp);
	}

	@Override
	public void handle(INetHandlerUniversalServer handler) {
		handler.handleKeepAlive(this);
	}

	public long getTimestamp() {
		return timestamp;
	}
}