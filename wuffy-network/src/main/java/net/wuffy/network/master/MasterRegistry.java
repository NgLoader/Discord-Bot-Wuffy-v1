package net.wuffy.network.master;

import net.wuffy.network.master.client.CPacketMasterStartShard;
import net.wuffy.network.master.client.CPacketMasterStatsUpdate;
import net.wuffy.network.master.client.CPacketMasterStopShard;
import net.wuffy.network.master.server.SPacketMasterHallo;
import net.wuffy.network.master.server.SPacketMasterStatsUpdate;
import net.wuffy.network.master.server.SPacketMasterStoppedShard;
import net.wuffy.network.master.server.SPacketMasterSystemUpdate;
import net.wuffy.network.universal.UniversalRegistry;

public final class MasterRegistry extends UniversalRegistry {

	public static final MasterRegistry INSTANCE = new MasterRegistry();

	protected MasterRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketMasterStartShard.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketMasterStopShard.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketMasterStatsUpdate.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketMasterStoppedShard.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketMasterSystemUpdate.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketMasterStatsUpdate.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketMasterHallo.class);
	}
}