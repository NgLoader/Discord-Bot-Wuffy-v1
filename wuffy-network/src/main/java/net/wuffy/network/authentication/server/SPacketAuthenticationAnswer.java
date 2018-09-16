package net.wuffy.network.authentication.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.authentication.INetHandlerAuthenticationServer;

public class SPacketAuthenticationAnswer implements Packet<INetHandlerAuthenticationServer> {

	private byte[] token;

	public SPacketAuthenticationAnswer() { }

	public SPacketAuthenticationAnswer(byte[] token) {
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
	public void handle(INetHandlerAuthenticationServer handler) {
		handler.handleAuthenticationAnswer(this);
	}

	public byte[] getToken() {
		return token;
	}
}