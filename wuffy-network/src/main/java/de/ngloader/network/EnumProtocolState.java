package de.ngloader.network;

import de.ngloader.network.PacketRegistry.EnumProtocolDirection;
import de.ngloader.network.authentication.AuthenticationRegistry;
import de.ngloader.network.music.MusicRegistry;

/**
 * @author Ingrim4
 */
public enum EnumProtocolState {

	AUTH(AuthenticationRegistry.INSTANCE),
	MUSIC(MusicRegistry.INSTANCE);

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