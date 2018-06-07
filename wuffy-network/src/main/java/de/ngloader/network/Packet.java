package de.ngloader.network;

import java.io.IOException;

/**
 * @author Ingrim4
 */
public interface Packet<T extends INetHandler> {

	void read(PacketBuffer packetBuffer) throws IOException;
	void write(PacketBuffer packetBuffer) throws IOException;
	void handle(T handler);
}