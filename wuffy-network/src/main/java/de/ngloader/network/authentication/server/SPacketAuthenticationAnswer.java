package de.ngloader.network.authentication.server;

import java.io.IOException;

import de.ngloader.network.Packet;
import de.ngloader.network.PacketBuffer;
import de.ngloader.network.authentication.INetHandlerAuthenticationServer;

/**
 * @author Ingrim4
 */
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