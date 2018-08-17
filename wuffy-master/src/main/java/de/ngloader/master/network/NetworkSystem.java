package de.ngloader.master.network;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.ngloader.common.util.ITickable;
import de.ngloader.network.AbstractServer;
import de.ngloader.network.EnumProtocolState;
import de.ngloader.network.NettyLengthPrepender;
import de.ngloader.network.NettyLengthSplitter;
import de.ngloader.network.NettyPacketDecoder;
import de.ngloader.network.NettyPacketEncoder;
import de.ngloader.network.NetworkManager;
import de.ngloader.network.PacketRegistry.EnumProtocolDirection;
import de.ngloader.network.authentication.client.CPacketAuthenticationDisconnect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NetworkSystem extends AbstractServer implements ITickable {

	private static final String DISCONNECT_REASON = "Internal server error";
	private final List<NetworkManager> networkManager = new ArrayList<NetworkManager>();

	public NetworkSystem start(InetSocketAddress address) {
		this.start(address, 0, new ChannelInitializer<Channel>() {

			protected void initChannel(Channel channel) throws Exception {
				channel.config().setOption(ChannelOption.TCP_NODELAY, true);
				channel.pipeline()
					.addLast("timeout", new ReadTimeoutHandler(30))
					.addLast("splitter", new NettyLengthSplitter())
					.addLast("decoder", new NettyPacketDecoder(EnumProtocolDirection.SERVERBOUND))
					.addLast("prepender", new NettyLengthPrepender())
					.addLast("encoder", new NettyPacketEncoder(EnumProtocolDirection.CLIENTBOUND));

				NetworkManager manager = new NetworkManager();
				NetworkSystem.this.networkManager.add(manager);
				channel.pipeline().addLast("packet_handler", manager);
				manager.setNetHandler(new NetHandlerAuthenticationServer(manager));
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
}