package de.ngloader.core.network;

import java.util.List;

import de.ngloader.core.logger.Logger;
import de.ngloader.core.logger.LoggerManager;
import de.ngloader.core.network.PacketRegistry.EnumProtocolDirection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

/**
 * @author Ingrim4
 */
public class NettyPacketDecoder extends ByteToMessageDecoder {

	private EnumProtocolDirection protocolDirection;

	public NettyPacketDecoder(EnumProtocolDirection protocolDirection) {
		this.protocolDirection = protocolDirection;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() != 0) {
			PacketBuffer packetBuffer = new PacketBuffer(in);
			int packetId = packetBuffer.readVarInt();

			EnumProtocolState protocolState = ctx.channel().attr(NetworkManager.PROTOCOL).get();
			Packet<?> packet = protocolState.getById(this.protocolDirection, packetId);
			if(packet == null)
				throw new DecoderException(String.format("Bad packet id %d", packetId));

			packet.read(packetBuffer);
			if(packetBuffer.readableBytes() != 0)
				throw new DecoderException(String.format("Packet %s/%d (%s) was larger than expected, found %d bytes extra whilst reading packet", protocolState, packetId, packet.getClass().getSimpleName(), packetBuffer.readableBytes()));

			out.add(packet);
			if(LoggerManager.isDebug())
				Logger.debug(String.format("Decoder/%s/%s", protocolState, packetId), String.format("IN: %s", packet.getClass().getName()));
		}
	}
}