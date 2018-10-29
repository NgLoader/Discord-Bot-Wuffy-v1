package net.wuffy.network.loadbalancer;

import net.wuffy.network.INetHandler;
import net.wuffy.network.loadbalancer.client.CPacketLoadBalancerMasterUpdate;

public interface INetHandlerLoadBalancerClient extends INetHandler {

	public void handleLoadBalancerMasterUpdate(CPacketLoadBalancerMasterUpdate loadBalancerMasterUpdate);
}