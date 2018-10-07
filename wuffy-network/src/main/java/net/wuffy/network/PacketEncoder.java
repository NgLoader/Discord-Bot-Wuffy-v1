package net.wuffy.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.EncoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import net.wuffy.network.PacketRegistry.EnumProtocolDirection;

/**
 * @author Ingrim4
 */
public class PacketEncoder extends MessageToByteEncoder<Packet<?>> {

	private EnumProtocolDirection protocolDirection;

	public PacketEncoder(EnumProtocolDirection protocolDirection) {
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
	}
}