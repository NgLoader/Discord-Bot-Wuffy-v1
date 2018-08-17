package de.ngloader.network.universal.client;

import java.io.IOException;

import de.ngloader.network.Packet;
import de.ngloader.network.PacketBuffer;
import de.ngloader.network.universal.INetHandlerUniversalClient;

/**
 * @author Ingrim4
 */
public class CPacketUniversalKeepAlive implements Packet<INetHandlerUniversalClient> {

	private long timestamp;

	public CPacketUniversalKeepAlive() { }

	public CPacketUniversalKeepAlive(long timestamp) {
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
	public void handle(INetHandlerUniversalClient handler) {
		handler.handleKeepAlive(this);
	}

	public long getTimestamp() {
		return timestamp;
	}
}
