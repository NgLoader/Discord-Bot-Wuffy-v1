package net.wuffy.network;

import net.wuffy.network.PacketRegistry.EnumProtocolDirection;

/**
 * @author Ingrim4
 */
public enum EnumProtocolState {

	AUTH(null);

	private PacketRegistry registry;

	private EnumProtocolState(PacketRegistry registry) {
		this.registry = registry;
	}

	public int getId(EnumProtocolDirection protocolDirection, Packet<?> packet) {
		return this.registry.getId(protocolDirection, packet);
	}

	public Packet<?> getById(EnumProtocolDirection protocolDirection, int id) throws Exception {
		return this.registry.getById(protocolDirection, id);
	}
}