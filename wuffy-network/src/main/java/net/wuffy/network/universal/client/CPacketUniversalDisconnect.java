package net.wuffy.network.universal.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.universal.INetHandlerUniversalClient;

public class CPacketUniversalDisconnect implements Packet<INetHandlerUniversalClient> {

	private String reason;

	public CPacketUniversalDisconnect() { }

	public CPacketUniversalDisconnect(String reason) {
		this.reason = reason;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.reason = packetBuffer.readString();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeString(this.reason);
	}

	@Override
	public void handle(INetHandlerUniversalClient handler) {
		handler.handleDisconnect(this);
	}

	public String getReason() {
		return reason;
	}
}