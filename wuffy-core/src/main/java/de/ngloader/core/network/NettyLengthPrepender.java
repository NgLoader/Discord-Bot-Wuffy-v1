package de.ngloader.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Ingrim4
 */
public class NettyLengthPrepender extends MessageToByteEncoder<ByteBuf> {

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		int bodyLength = msg.readableBytes();
		int headerLength = PacketBuffer.getVarIntSize(bodyLength);

		PacketBuffer packetDataSerializer = new PacketBuffer(out);
		packetDataSerializer.ensureWritable(bodyLength + headerLength);
		packetDataSerializer.writeVarInt(bodyLength);
		packetDataSerializer.writeBytes(msg, msg.readerIndex(), bodyLength);
	}
}
