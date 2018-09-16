package net.wuffy.network.authentication;

import net.wuffy.network.PacketRegistry;
import net.wuffy.network.authentication.client.CPacketAuthenticationChallenge;
import net.wuffy.network.authentication.client.CPacketAuthenticationDisconnect;
import net.wuffy.network.authentication.client.CPacketAuthenticationSuccess;
import net.wuffy.network.authentication.server.SPacketAuthenticationAnswer;
import net.wuffy.network.authentication.server.SPacketAuthenticationStart;

public final class AuthenticationRegistry extends PacketRegistry {

	public static final AuthenticationRegistry INSTANCE = new AuthenticationRegistry();

	protected AuthenticationRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketAuthenticationChallenge.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketAuthenticationSuccess.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketAuthenticationDisconnect.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketAuthenticationStart.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketAuthenticationAnswer.class);
	}
}