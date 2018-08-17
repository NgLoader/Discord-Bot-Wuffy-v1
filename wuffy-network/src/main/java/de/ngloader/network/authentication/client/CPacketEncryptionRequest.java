package de.ngloader.network.authentication.client;

import java.io.IOException;
import java.security.PublicKey;

import de.ngloader.common.util.CryptUtil;
import de.ngloader.network.Packet;
import de.ngloader.network.PacketBuffer;
import de.ngloader.network.authentication.INetHandlerAuthenticationClient;

/**
 * @author Ingrim4
 */
public class CPacketEncryptionRequest implements Packet<INetHandlerAuthenticationClient> {

	private PublicKey publicKey;
	private byte[] token;

	public CPacketEncryptionRequest() { }

	public CPacketEncryptionRequest(PublicKey publicKey, byte[] token) {
		this.publicKey = publicKey;
		this.token = token;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.publicKey = CryptUtil.generatePublicKey(packetBuffer.readByteArray());
		this.token = packetBuffer.readByteArray();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeByteArray(this.publicKey.getEncoded());
		packetBuffer.writeByteArray(this.token);
	}

	@Override
	public void handle(INetHandlerAuthenticationClient handler) {
		handler.handleEncryptionRequest(this);
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public byte[] getToken() {
		return token;
	}
}