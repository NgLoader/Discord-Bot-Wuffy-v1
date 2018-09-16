package net.wuffy.network;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;

/**
 * @author Ingrim4
 */
public abstract class AbstractServer {

	protected final List<ChannelFuture> serverChannel = new ArrayList<ChannelFuture>();

	protected void start(SocketAddress address, int threads, ChannelHandler channelHandler) {
		synchronized (this.serverChannel) {
			this.serverChannel.add(
				new ServerBootstrap()
					.group(NettyUtil.createEventLoopGroup(threads))
					.channel(NettyUtil.getServerChannelClass())
					.childHandler(channelHandler)
					.localAddress(address)
					.bind()
					.syncUninterruptibly()
			);
		}
	}

	public final void close() {
		for(ChannelFuture channelFuture : this.serverChannel)
			try {
				channelFuture.channel().close().sync();
			} catch (InterruptedException e) {
				throw new Error("Unable to stop server", e);
			}
	}
}