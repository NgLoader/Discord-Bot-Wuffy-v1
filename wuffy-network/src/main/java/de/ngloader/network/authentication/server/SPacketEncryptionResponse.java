package de.ngloader.network.authentication.server;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.ngloader.common.util.CryptUtil;
import de.ngloader.network.Packet;
import de.ngloader.network.PacketBuffer;
import de.ngloader.network.authentication.INetHandlerAuthenticationServer;

/**
 * @author Ingrim4
 */
public class SPacketEncryptionResponse implements Packet<INetHandlerAuthenticationServer> {

	private byte[] secretKey, token;

	public SPacketEncryptionResponse() { }

	public SPacketEncryptionResponse(PublicKey publicKey, SecretKey secretKey, byte[] token) {
		this.secretKey = CryptUtil.crytpBytes(Cipher.ENCRYPT_MODE, publicKey, secretKey.getEncoded());
		this.token = CryptUtil.crytpBytes(Cipher.ENCRYPT_MODE, publicKey, token);
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.secretKey = packetBuffer.readByteArray();
		this.token = packetBuffer.readByteArray();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeByteArray(this.secretKey);
		packetBuffer.writeByteArray(this.token);
	}

	@Override
	public void handle(INetHandlerAuthenticationServer handler) {
		handler.handleEncryptionResponse(this);
	}

	public SecretKey getSecretKey(PrivateKey privateKey) {
		return new SecretKeySpec(CryptUtil.crytpBytes(Cipher.DECRYPT_MODE, privateKey, this.secretKey), "AES");
	}

	public byte[] getToken(PrivateKey privateKey) {
		return CryptUtil.crytpBytes(Cipher.DECRYPT_MODE, privateKey, this.token);
	}
}