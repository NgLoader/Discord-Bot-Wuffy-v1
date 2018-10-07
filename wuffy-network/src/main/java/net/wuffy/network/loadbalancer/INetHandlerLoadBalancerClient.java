package net.wuffy.network.loadbalancer;

import net.wuffy.network.INetHandler;
import net.wuffy.network.loadbalancer.client.CPacketLoadBalancerNowMaster;

public interface INetHandlerLoadBalancerClient extends INetHandler {

	public void handleLoadBalancerNowMaster(CPacketLoadBalancerNowMaster loadBalancerNowMaster);
}