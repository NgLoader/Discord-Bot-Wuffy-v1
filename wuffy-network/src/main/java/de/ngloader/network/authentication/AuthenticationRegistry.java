package de.ngloader.network.authentication;

import de.ngloader.network.PacketRegistry;
import de.ngloader.network.authentication.client.CPacketAuthenticationChallenge;
import de.ngloader.network.authentication.client.CPacketAuthenticationDisconnect;
import de.ngloader.network.authentication.client.CPacketAuthenticationSuccess;
import de.ngloader.network.authentication.client.CPacketEncryptionRequest;
import de.ngloader.network.authentication.server.SPacketAuthenticationAnswer;
import de.ngloader.network.authentication.server.SPacketAuthenticationStart;
import de.ngloader.network.authentication.server.SPacketEncryptionResponse;

/**
 * @author Ingrim4
 */
public final class AuthenticationRegistry extends PacketRegistry {

	public static final AuthenticationRegistry INSTANCE = new AuthenticationRegistry();

	protected AuthenticationRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketEncryptionRequest.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketAuthenticationChallenge.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketAuthenticationSuccess.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketAuthenticationDisconnect.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketAuthenticationStart.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketEncryptionResponse.class);
		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketAuthenticationAnswer.class);
	}
}