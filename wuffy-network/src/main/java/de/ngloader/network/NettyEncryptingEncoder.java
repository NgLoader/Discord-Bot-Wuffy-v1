package de.ngloader.network;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import de.ngloader.common.util.CryptUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Ingrim4
 */
public class NettyEncryptingEncoder extends MessageToByteEncoder<ByteBuf> {

	private final Cipher cipher;
	private byte[] inputBuffer = new byte[0];
	private byte[] outputBuffer = new byte[0];

	public NettyEncryptingEncoder(SecretKey secretKey) {
		this.cipher = CryptUtil.createNetworkCipher(Cipher.ENCRYPT_MODE, secretKey);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) throws Exception {
		int size = in.readableBytes();
		if(size > this.inputBuffer.length)
			this.inputBuffer = new byte[size];
		in.readBytes(this.inputBuffer, 0, size);

		int outputSize = this.cipher.getOutputSize(size);
		if(outputSize > this.outputBuffer.length)
			this.outputBuffer = new byte[outputSize];
		out.writeBytes(this.outputBuffer, 0, this.cipher.update(this.inputBuffer, 0, size, this.outputBuffer));
	}
}