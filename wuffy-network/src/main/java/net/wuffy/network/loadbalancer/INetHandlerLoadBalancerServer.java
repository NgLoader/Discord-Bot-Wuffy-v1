package net.wuffy.network.loadbalancer;

import net.wuffy.network.INetHandler;
import net.wuffy.network.loadbalancer.server.SPacketLoadBalancerInit;

public interface INetHandlerLoadBalancerServer extends INetHandler {

	public void handleLoadBalancerInit(SPacketLoadBalancerInit loadBalancerInit);
}