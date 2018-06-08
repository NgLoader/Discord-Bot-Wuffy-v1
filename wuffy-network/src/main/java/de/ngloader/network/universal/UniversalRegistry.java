package de.ngloader.network.universal;

import de.ngloader.network.PacketRegistry;
import de.ngloader.network.universal.client.CPacketUniversalDisconnect;
import de.ngloader.network.universal.client.CPacketUniversalKeepAlive;
import de.ngloader.network.universal.server.SPacketUniversalKeepAlive;

public abstract class UniversalRegistry extends PacketRegistry {

	protected UniversalRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketUniversalKeepAlive.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketUniversalDisconnect.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketUniversalKeepAlive.class);
	}
}