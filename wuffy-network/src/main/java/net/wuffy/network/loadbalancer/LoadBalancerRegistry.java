package net.wuffy.network.loadbalancer;

import net.wuffy.network.loadbalancer.client.CPacketLoadBalancerMasterUpdate;
import net.wuffy.network.loadbalancer.server.SPacketLoadBalancerInit;
import net.wuffy.network.universal.UniversalRegistry;

public final class LoadBalancerRegistry extends UniversalRegistry {

	public static final LoadBalancerRegistry INSTANCE = new LoadBalancerRegistry();

	protected LoadBalancerRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketLoadBalancerMasterUpdate.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketLoadBalancerInit.class);
	}
}