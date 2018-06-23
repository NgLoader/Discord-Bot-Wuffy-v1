package de.ngloader.core.network;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ingrim4
 */
public abstract class PacketRegistry {

	private final List<Class<? extends Packet<?>>> serverbound = new ArrayList<Class<? extends Packet<?>>>();
	private final List<Class<? extends Packet<?>>> clientbound = new ArrayList<Class<? extends Packet<?>>>();

	private final List<Class<? extends Packet<?>>> getDirection(EnumProtocolDirection protocolDirection) {
		return (protocolDirection == EnumProtocolDirection.SERVERBOUND ? this.serverbound : this.clientbound);
	}

	protected final void addPacket(EnumProtocolDirection protocolDirection, Class<? extends Packet<?>> packet) {
		this.getDirection(protocolDirection).add(packet);
	}

	public final int getId(EnumProtocolDirection protocolDirection, Packet<?> packet) {
		return this.getDirection(protocolDirection).indexOf(packet.getClass());
	}

	public final Packet<?> getById(EnumProtocolDirection protocolDirection, int id) throws Exception {
		return this.getDirection(protocolDirection).get(id).getConstructor().newInstance();
	}

	public enum EnumProtocolDirection {
		SERVERBOUND, CLIENTBOUND;
	}
}
