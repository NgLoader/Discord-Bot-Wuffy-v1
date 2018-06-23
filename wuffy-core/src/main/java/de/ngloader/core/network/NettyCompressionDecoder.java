package de.ngloader.core.network;

import java.util.List;
import java.util.zip.Inflater;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

/**
 * @author Ingrim4
 */
public class NettyCompressionDecoder extends ByteToMessageDecoder {

	private static final int MAX_SIZE = 2 * 1024 * 1024; //2Mib

	private final Inflater inflater;
	private int threshold;

	public NettyCompressionDecoder(int threshold) {
		this.threshold = threshold;
		this.inflater = new Inflater();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() != 0) {
			PacketBuffer packetBuffer = new PacketBuffer(in);
			int size = packetBuffer.readVarInt();

			if(size == 0) {
				out.add(packetBuffer.readBytes(packetBuffer.readableBytes()));
			} else {
				if(size < this.threshold) {
					throw new DecoderException(String.format("Badly compressed packet - size of %d is below server threshold of %d", size, this.threshold));
				} else if(size > MAX_SIZE) {
					throw new DecoderException(String.format("Badly compressed packet - size of %d is larger than protocol maximum of %d", size, MAX_SIZE));
				}

				byte[] buffer = new byte[packetBuffer.readableBytes()];
				packetBuffer.readBytes(buffer);
				this.inflater.setInput(buffer, 0, buffer.length);

				ByteBuf packet = ctx.alloc().heapBuffer(size);
				this.inflater.inflate(packet.array(), packet.arrayOffset(), size);
				this.inflater.reset();
				out.add(packet);
			}
		}
	}
}
