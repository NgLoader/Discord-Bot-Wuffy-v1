package de.ngloader.network;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import de.ngloader.common.util.CryptUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author Ingrim4
 */
public class NettyEncryptingDecoder extends ByteToMessageDecoder {

	private final Cipher cipher;
	private byte[] inputBuffer = new byte[0];

	public NettyEncryptingDecoder(SecretKey secretKey) {
		this.cipher = CryptUtil.createNetworkCipher(Cipher.DECRYPT_MODE, secretKey);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int size = in.readableBytes();
		if(size > this.inputBuffer.length)
			this.inputBuffer = new byte[size];
		in.readBytes(this.inputBuffer, 0, size);

		ByteBuf buffer = ctx.alloc().heapBuffer(this.cipher.getOutputSize(size));
		buffer.writerIndex(this.cipher.update(this.inputBuffer, 0, size, buffer.array(), buffer.arrayOffset()));
		out.add(buffer);
	}
}