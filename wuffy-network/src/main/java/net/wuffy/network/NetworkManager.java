package net.wuffy.network;

import java.net.InetSocketAddress;
import java.util.Objects;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.ITickable;

/**
 * @author Ingrim4
 */
public class NetworkManager extends SimpleChannelInboundHandler<Packet<INetHandler>> implements ITickable {

	public static final AttributeKey<EnumProtocolState> PROTOCOL = AttributeKey.valueOf("protocol");

	private Channel channel;
	private boolean needsFlush;
	private INetHandler handler;
	private boolean disconnected;
	private String disconnectReason;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.channel = ctx.channel();

		this.setProtocol(EnumProtocolState.AUTH);

		if(this.handler instanceof IInitNetHandler)
			((IInitNetHandler)this.handler).onChannelActive();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet<INetHandler> packet) throws Exception {
		packet.handle(this.handler);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		this.needsFlush = false;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.close(String.format("%s lost connection", this.channel.toString()));
	}

	public InetSocketAddress getAddress() {
		return (InetSocketAddress) this.channel.remoteAddress();
	}

	public void sendPacket(Packet<?> packet) {
		this.sendPacket(packet, (GenericFutureListener<? extends Future<? super Void>>[])null);
	}

	@SafeVarargs
	public final void sendPacket(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>... listeners) {
		this.dispatchPacket(packet, listeners);
	}

	private void dispatchPacket(final Packet<?> packet, GenericFutureListener<? extends Future<? super Void>>[] listeners) {
		final ChannelPromise promise = listeners == null ? channel.voidPromise() : channel.newPromise();
		if(this.channel.eventLoop().inEventLoop()) {
			ChannelFuture channelFuture = this.channel.write(packet, promise);
			if(listeners != null)
				channelFuture.addListeners(listeners);
			this.needsFlush = true;
		} else {
			this.channel.eventLoop().execute(() -> {
				ChannelFuture channelFuture = this.channel.write(packet, promise);
				if(listeners != null)
					channelFuture.addListeners(listeners);
				this.needsFlush = true;
			});
		}
	}

	public void handleDisconnect() {
		if(this.channel != null && !this.channel.isOpen()) {
			if(this.disconnected) {
				Logger.warn("handleDisconnection() called twice");
			} else {
				this.disconnected = true;
				if(this.handler != null)
					this.handler.onDisconnect(this.disconnectReason == null ? "Internal server error" : this.disconnectReason);
			}
		}
	}

	@Override
	public void update() {
		if(this.handler instanceof ITickable)
			((ITickable) this.handler).update();

		if(this.channel != null && this.needsFlush)
			this.flush();
	}

	public void flush() {
		this.channel.flush();
		this.needsFlush = false;
	}

	public void stopReading() {
		this.channel.config().setAutoRead(false);
	}

	public boolean hasChannel() {
		return this.channel != null;
	}

	public boolean isConnected() {
		return this.channel != null && this.channel.isOpen();
	}

	public Channel getChannel() {
		return this.channel;
	}

	public void close(String reason) {
		if(this.channel.isOpen())
			this.channel.close().awaitUninterruptibly();
		this.disconnectReason = reason;
	}

	public void setNetHandler(INetHandler handler) {
		Objects.nonNull(handler);
		if(LoggerManager.isDebug())
			Logger.debug("NetworkManager", String.format("Set handler of %s to %s", this, handler));
		this.handler = handler;
	}

	public void setProtocol(EnumProtocolState protocolState) {
		this.channel.attr(NetworkManager.PROTOCOL).set(protocolState);
	}

	public INetHandler getHandler() {
		return this.handler;
	}

	public EnumProtocolState getProtocol() {
		return this.channel.attr(NetworkManager.PROTOCOL).get();
	}
}