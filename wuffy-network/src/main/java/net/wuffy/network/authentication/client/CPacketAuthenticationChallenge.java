package net.wuffy.network.authentication.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.authentication.INetHandlerAuthenticationClient;

public class CPacketAuthenticationChallenge implements Packet<INetHandlerAuthenticationClient> {

	private byte[] token;

	public CPacketAuthenticationChallenge() { }

	public CPacketAuthenticationChallenge(byte[] token) {
		this.token = token;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.token = packetBuffer.readByteArray();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeByteArray(this.token);
	}

	@Override
	public void handle(INetHandlerAuthenticationClient handler) {
		handler.handleAuthenticationChallenge(this);
	}

	public byte[] getToken() {
		return token;
	}
}