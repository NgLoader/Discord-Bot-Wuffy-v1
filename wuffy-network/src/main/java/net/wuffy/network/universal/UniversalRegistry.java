package net.wuffy.network.universal;

import net.wuffy.network.PacketRegistry;
import net.wuffy.network.universal.client.CPacketUniversalDisconnect;
import net.wuffy.network.universal.client.CPacketUniversalKeepAlive;
import net.wuffy.network.universal.server.SPacketUniversalKeepAlive;

public abstract class UniversalRegistry extends PacketRegistry {

	protected UniversalRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketUniversalKeepAlive.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketUniversalDisconnect.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketUniversalKeepAlive.class);
	}
}