package de.ngloader.network.authentication.client;

import java.io.IOException;

import de.ngloader.network.Packet;
import de.ngloader.network.PacketBuffer;
import de.ngloader.network.authentication.INetHandlerAuthenticationClient;

/**
 * @author Ingrim4
 */
public class CPacketAuthenticationSuccess implements Packet<INetHandlerAuthenticationClient> {

	private int threshold;

	public CPacketAuthenticationSuccess() { }

	public CPacketAuthenticationSuccess(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.threshold = packetBuffer.readVarInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeVarInt(this.threshold);
	}

	@Override
	public void handle(INetHandlerAuthenticationClient handler) {
		handler.handleAuthenticationSuccess(this);
	}

	public int getThreshold() {
		return threshold;
	}
}