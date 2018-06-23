package de.ngloader.core.network;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

/**
 * @author Ingrim4
 */
public abstract class AbstractClient {

	protected final List<ChannelFuture> clientChannel = new ArrayList<ChannelFuture>();

	protected void start(SocketAddress address, int threads, ChannelHandler channelHandler) {
		synchronized (this.clientChannel) {
			this.clientChannel.add(
				new Bootstrap()
					.group(NettyUtil.createEventLoopGroup(threads)).channel(NettyUtil.getClientChannelClass())
					.handler(channelHandler).remoteAddress(address).connect().syncUninterruptibly()
			);
		}
	}

	public void close() {
		for(ChannelFuture channelFuture : this.clientChannel)
			try {
				channelFuture.channel().close().sync();
			} catch (InterruptedException e) {
				throw new Error("Unable to stop server", e);
			}
	}
}