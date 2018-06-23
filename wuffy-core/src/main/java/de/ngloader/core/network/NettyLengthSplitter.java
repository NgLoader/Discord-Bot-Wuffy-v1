package de.ngloader.core.network;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;

/**
 * @author Ingrim4
 */
public class NettyLengthSplitter extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		byte[] lengthArray = new byte[5];
		for (int i = 0; i < lengthArray.length; ++i) {
			if (!in.isReadable()) {
				in.resetReaderIndex();
				return;
			}

			lengthArray[i] = in.readByte();
			if (lengthArray[i] > -1) { //ensure the first bit is 0
				try {
					int length = readVarInt(lengthArray);
					if (in.readableBytes() >= length) {
						out.add(in.readBytes(length));
						return;
					}
					in.resetReaderIndex();
				} finally {
					lengthArray = null;
				}
				return;
			}
		}
	}

	private int readVarInt(byte[] array) {
		int value = 0;
		int bytes = 0;
		while (true) {
			byte in = array[bytes];
			value |= (in & 0x7F) << (bytes++ * 7);
			if (bytes > 5)
				throw new DecoderException("Attempt to read int bigger than allowed for a varint!");
			if ((in & 0x80) != 0x80)
				break;
		}
		return value;
	}
}
