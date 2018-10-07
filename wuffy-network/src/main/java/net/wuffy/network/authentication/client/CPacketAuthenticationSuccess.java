package net.wuffy.network.authentication.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.authentication.INetHandlerAuthenticationClient;

public class CPacketAuthenticationSuccess implements Packet<INetHandlerAuthenticationClient> {

	public CPacketAuthenticationSuccess() { }

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException { }

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException { }

	@Override
	public void handle(INetHandlerAuthenticationClient handler) {
		handler.handleAuthenticationSuccess(this);
	}
}