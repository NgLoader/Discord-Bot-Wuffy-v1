package net.wuffy.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import net.wuffy.network.PacketRegistry.EnumProtocolDirection;

public class PacketDecoder extends ByteToMessageDecoder {

	private EnumProtocolDirection protocolDirection;

	public PacketDecoder(EnumProtocolDirection protocolDirection) {
		this.protocolDirection = protocolDirection;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		if(buffer.readableBytes() != 0) {
			PacketBuffer packetBuffer = new PacketBuffer(buffer);
			int packetId = packetBuffer.readVarInt();

			EnumProtocolState protocolState = ctx.channel().attr(NetworkManager.PROTOCOL).get();
			Packet<?> packet = protocolState.getById(this.protocolDirection, packetId);

			if(packet == null)
				throw new DecoderException(String.format("Bad packet id %d", packetId));

			packet.read(packetBuffer);
			if(packetBuffer.readableBytes() != 0)
				throw new DecoderException(String.format("Packet %s/%d (%s) was larger than expected, found %d bytes extra whilst reading packet", protocolState, packetId, packet.getClass().getSimpleName(), packetBuffer.readableBytes()));

			out.add(packet);
		}
	}
}