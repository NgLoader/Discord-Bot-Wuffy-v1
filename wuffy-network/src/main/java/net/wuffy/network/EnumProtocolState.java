package net.wuffy.network;

import net.wuffy.network.PacketRegistry.EnumProtocolDirection;
import net.wuffy.network.authentication.AuthenticationRegistry;
import net.wuffy.network.bot.BotRegistry;
import net.wuffy.network.loadbalancer.LoadBalancerRegistry;
import net.wuffy.network.music.MusicRegistry;

/**
 * @author Ingrim4
 */
public enum EnumProtocolState {

	AUTH(AuthenticationRegistry.INSTANCE),
	LOADBALANCER(LoadBalancerRegistry.INSTANCE),
	BOT(BotRegistry.INSTANCE),
	MUSIC(MusicRegistry.INSTANCE);

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