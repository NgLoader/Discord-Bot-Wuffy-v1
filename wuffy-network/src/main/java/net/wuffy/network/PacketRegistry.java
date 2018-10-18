package net.wuffy.network;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import net.wuffy.common.logger.Logger;

/**
 * @inspiration Ingrim4 
 */
public abstract class PacketRegistry {

	private final Map<Integer, Constructor<? extends Packet<?>>> serverboundById = new HashMap<Integer, Constructor<? extends Packet<?>>>();
	private final Map<Class<? extends Packet<?>>, Integer> serverboundByClass = new HashMap<Class<? extends Packet<?>>, Integer>();

	private final Map<Integer, Constructor<? extends Packet<?>>> clientboundById = new HashMap<Integer, Constructor<? extends Packet<?>>>();
	private final Map<Class<? extends Packet<?>>, Integer> clientboundByClass = new HashMap<Class<? extends Packet<?>>, Integer>();

	private final Map<Integer, Constructor<? extends Packet<?>>> getDirectionById(EnumProtocolDirection protocolDirection) {
		return protocolDirection == EnumProtocolDirection.SERVERBOUND ? this.serverboundById : this.clientboundById;
	}

	private final Map<Class<? extends Packet<?>>, Integer> getDirectionByClass(EnumProtocolDirection protocolDirection) {
		return protocolDirection == EnumProtocolDirection.SERVERBOUND ? this.serverboundByClass : this.clientboundByClass;
	}

	protected final void addPacket(EnumProtocolDirection protocolDirection, Class<? extends Packet<?>> packet) {
		Map<Class<? extends Packet<?>>, Integer> boundByClass = this.getDirectionByClass(protocolDirection);
		Map<Integer, Constructor<? extends Packet<?>>> boundById = this.getDirectionById(protocolDirection);

		int id = boundByClass.size();

		while (boundById.containsKey(id) || boundByClass.containsValue(id))
			id++;

		try {
			boundById.put(id, packet.getConstructor());
			boundByClass.put(packet, id);
		} catch (NoSuchMethodException | SecurityException e) {
			Logger.fatal("PacketRegestry", String.format("Error by adding packet \"%s\"", packet.getClass().getSimpleName()), e);
		}
	}

	public final int getId(EnumProtocolDirection protocolDirection, Packet<?> packet) {
		return this.getDirectionByClass(protocolDirection).get(packet.getClass());
	}

	public final Packet<?> getById(EnumProtocolDirection protocolDirection, int id) throws Exception {
		return this.getDirectionById(protocolDirection).get(id).newInstance();
	}

	public enum EnumProtocolDirection {
		SERVERBOUND, CLIENTBOUND;
	}
}