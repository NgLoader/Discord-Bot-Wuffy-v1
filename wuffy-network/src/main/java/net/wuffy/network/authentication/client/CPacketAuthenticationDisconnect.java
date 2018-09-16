package net.wuffy.network.authentication.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.authentication.INetHandlerAuthenticationClient;

public class CPacketAuthenticationDisconnect implements Packet<INetHandlerAuthenticationClient> {

	private String reason;

	public CPacketAuthenticationDisconnect() { }

	public CPacketAuthenticationDisconnect(String reason) {
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
	public void handle(INetHandlerAuthenticationClient handler) {
		handler.handleAuthenticationDisconnect(this);
	}

	public String getReason() {
		return reason;
	}
}