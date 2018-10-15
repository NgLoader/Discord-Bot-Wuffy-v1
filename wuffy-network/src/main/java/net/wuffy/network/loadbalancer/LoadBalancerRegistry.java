package net.wuffy.network.loadbalancer;

import net.wuffy.network.loadbalancer.client.CPacketLoadBalancerNowMaster;
import net.wuffy.network.loadbalancer.server.SPacketLoadBalancerInit;
import net.wuffy.network.universal.UniversalRegistry;

public final class LoadBalancerRegistry extends UniversalRegistry {

	public static final LoadBalancerRegistry INSTANCE = new LoadBalancerRegistry();

	protected LoadBalancerRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketLoadBalancerNowMaster.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketLoadBalancerInit.class);
	}
}