package de.ngloader.network.authentication.client;

import java.io.IOException;

import de.ngloader.network.Packet;
import de.ngloader.network.PacketBuffer;
import de.ngloader.network.authentication.INetHandlerAuthenticationClient;

/**
 * @author Ingrim4
 */
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