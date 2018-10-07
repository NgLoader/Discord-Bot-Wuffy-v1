package net.wuffy.network.loadbalancer;

import net.wuffy.network.PacketRegistry;

public final class LoadBalancerRegistry extends PacketRegistry {

	public static final LoadBalancerRegistry INSTANCE = new LoadBalancerRegistry();

	protected LoadBalancerRegistry() {
	}
}