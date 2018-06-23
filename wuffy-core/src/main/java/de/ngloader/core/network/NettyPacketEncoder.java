package de.ngloader.core.network;

import de.ngloader.core.logger.Logger;
import de.ngloader.core.logger.LoggerManager;
import de.ngloader.core.network.PacketRegistry.EnumProtocolDirection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Ingrim4
 */
public class NettyPacketEncoder extends MessageToByteEncoder<Packet<?>> {

	private EnumProtocolDirection protocolDirection;

	public NettyPacketEncoder(EnumProtocolDirection protocolDirection) {
		this.protocolDirection = protocolDirection;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet<?> packet, ByteBuf out) throws Exception {
		EnumProtocolState protocolState = ctx.channel().attr(NetworkManager.PROTOCOL).get();

		int packetId = protocolState.getId(this.protocolDirection, packet);
		if(packetId == -1)
			throw new EncoderException(String.format("Can't serialize unregistered packet %s", packet.getClass().getSimpleName()));

		PacketBuffer packetBuffer = new PacketBuffer(out);
		packetBuffer.writeVarInt(packetId);
		packet.write(packetBuffer);

		if(LoggerManager.isDebug())
			Logger.debug(String.format("Encoder/%s/%s", protocolState, packetId), String.format("OUT: %s", packet.getClass().getName()));
	}
}