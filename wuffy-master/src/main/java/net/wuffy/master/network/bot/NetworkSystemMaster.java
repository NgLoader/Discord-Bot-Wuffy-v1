package net.wuffy.master.network.bot;

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
import net.wuffy.network.AbstractServer;
import net.wuffy.network.EnumProtocolState;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.PacketDecoder;
import net.wuffy.network.PacketEncoder;
import net.wuffy.network.PacketRegistry.EnumProtocolDirection;
import net.wuffy.network.authentication.client.CPacketAuthenticationDisconnect;
import net.wuffy.network.universal.client.CPacketUniversalDisconnect;

public class NetworkSystemMaster extends AbstractServer implements ITickable {

	private static final String DISCONNECT_REASON = "Internal server error";

	private final List<NetworkManager> networkManager = new ArrayList<NetworkManager>();

	public NetworkSystemMaster start(MasterConfig masterConfig) throws SSLException {
		SslContext sslContext = SslContextBuilder.forServer(new File(masterConfig.keyCertChainFile), new File(masterConfig.keyFile)).build();

		this.start(new InetSocketAddress(masterConfig.masterAddress, masterConfig.masterPort), 0, new ChannelInitializer<Channel>() {

			protected void initChannel(Channel channel) throws Exception {

				channel.config().setOption(ChannelOption.TCP_NODELAY, true);
				channel.pipeline()
					.addLast(sslContext.newHandler(channel.alloc()))
					.addLast("timeout", new ReadTimeoutHandler(30))
					.addLast("decoder", new PacketDecoder(EnumProtocolDirection.SERVERBOUND))
					.addLast("encoder", new PacketEncoder(EnumProtocolDirection.CLIENTBOUND));

				NetworkManager networkManager = new NetworkManager();
				NetworkSystemMaster.this.networkManager.add(networkManager);
				channel.pipeline().addLast("packet_handler", networkManager);
				networkManager.setNetHandler(new NetHandlerAuthenticationServer(networkManager));
			};
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
							EnumProtocolState protocolState = networkManager.getChannel().attr(NetworkManager.PROTOCOL).get();

							if(protocolState == EnumProtocolState.AUTH)
								networkManager.sendPacket(new CPacketAuthenticationDisconnect(DISCONNECT_REASON), future -> networkManager.close(DISCONNECT_REASON));
							else
								networkManager.sendPacket(new CPacketUniversalDisconnect(DISCONNECT_REASON), future -> networkManager.close(DISCONNECT_REASON));

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

	public List<NetworkManager> getNetworkManagers() {
		return this.networkManager;
	}
}