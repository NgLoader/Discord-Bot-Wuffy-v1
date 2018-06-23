package de.ngloader.core.network;

import de.ngloader.core.network.PacketRegistry.EnumProtocolDirection;
import de.ngloader.core.network.universal.UniversalRegistry;

/**
 * @author Ingrim4
 */
public enum EnumProtocolState {

//	AUTH(AuthenticationRegistry.INSTANCE),
//	MUSIC(MusicRegistry.INSTANCE);
	UNIVERSAL(UniversalRegistry.INSTANCE);

	private PacketRegistry registry;

	EnumProtocolState(PacketRegistry registry) {
		this.registry = registry;
	}

	public int getId(EnumProtocolDirection protocolDirection, Packet<?> packet) {
		return this.registry.getId(protocolDirection, packet);
	}

	public Packet<?> getById(EnumProtocolDirection protocolDirection, int id) throws Exception {
		return this.registry.getById(protocolDirection, id);
	}
}