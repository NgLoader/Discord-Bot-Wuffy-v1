package net.wuffy.master.network.loadbalancer;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLException;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.wuffy.common.util.ITickable;
import net.wuffy.master.MasterConfig;
import net.wuffy.network.AbstractClient;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.PacketDecoder;
import net.wuffy.network.PacketEncoder;
import net.wuffy.network.PacketRegistry.EnumProtocolDirection;

public class NetworkSystemLoadBalancer extends AbstractClient implements ITickable {

	private final List<NetworkManager> networkManager = new ArrayList<NetworkManager>();

	public NetworkSystemLoadBalancer start(MasterConfig config) throws SSLException {
		SslContext sslContext = SslContextBuilder.forClient().keyManager(new File(config.keyCertChainFile), new File(config.keyFile)).build();

		this.start(new InetSocketAddress(config.loadBalancerAddress, config.loadBalancerPort), 0, new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel channel) throws Exception {
				channel.config().setOption(ChannelOption.TCP_NODELAY, true);
				channel.pipeline()
					.addLast(sslContext.newHandler(channel.alloc()))
					.addLast("timeout", new ReadTimeoutHandler(30))
					.addLast("decoder", new PacketDecoder(EnumProtocolDirection.CLIENTBOUND))
					.addLast("encoder", new PacketEncoder(EnumProtocolDirection.SERVERBOUND));

				NetworkManager manager = new NetworkManager();
				NetworkSystemLoadBalancer.this.networkManager.add(manager);
				channel.pipeline().addLast("packet_handler", manager);
				manager.setNetHandler(new NetHandlerAuthenticationClient(manager));
			}
		});
		return this;
	}

	@Override
	public void update() {
		synchronized (this.networkManager) {
			for(Iterator<NetworkManager> iterator = this.networkManager.iterator(); iterator.hasNext();) {
				final NetworkManager networkManager = iterator.next();
				if(networkManager.hasChannel()) {
					if(networkManager.isConnected()) {
						try {
							networkManager.update();
						} catch (Exception e) {
							networkManager.close("Internal client error");
							networkManager.stopReading();
						}
					} else {
						iterator.remove();
						networkManager.handleDisconnect();
					}
				}
			}
		}
	}
}