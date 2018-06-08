package de.ngloader.network;

import java.util.zip.Deflater;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Ingrim4
 */
public class NettyCompressionEncoder extends MessageToByteEncoder<ByteBuf> {

	private final byte[] outputBuffer = new byte[8192];
	private final Deflater deflater;
	private int threshold;

	public NettyCompressionEncoder(int threshold) {
		this.threshold = threshold;
		this.deflater = new Deflater();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
		int size = in.readableBytes();
		PacketBuffer packetBuffer = new PacketBuffer(out);

		if(size < this.threshold) {
			packetBuffer.writeVarInt(0);
			packetBuffer.writeBytes(in);
		} else {
			byte[] inputBuffer = new byte[size];
			in.readBytes(inputBuffer);
			this.deflater.setInput(inputBuffer, 0, size);
			this.deflater.finish();

			packetBuffer.writeVarInt(size);
			while(!this.deflater.finished()) {
				int read = this.deflater.deflate(this.outputBuffer);
				packetBuffer.writeBytes(this.outputBuffer, 0, read);
			}

			this.deflater.reset();
		}
	}
}
