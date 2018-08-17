package de.ngloader.network.authentication.server;

import java.io.IOException;
import java.util.UUID;

import de.ngloader.network.Packet;
import de.ngloader.network.PacketBuffer;
import de.ngloader.network.authentication.INetHandlerAuthenticationServer;

/**
 * @author Ingrim4
 */
public class SPacketAuthenticationStart implements Packet<INetHandlerAuthenticationServer> {

	private UUID id;

	public SPacketAuthenticationStart() { }

	public SPacketAuthenticationStart(UUID id) {
		this.id = id;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.id = packetBuffer.readUUID();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeUUID(this.id);
	}

	@Override
	public void handle(INetHandlerAuthenticationServer handler) {
		handler.handleAuthenticationStart(this);
	}

	public UUID getId() {
		return id;
	}
}