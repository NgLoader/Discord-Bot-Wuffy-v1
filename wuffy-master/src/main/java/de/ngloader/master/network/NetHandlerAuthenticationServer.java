package de.ngloader.master.network;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.UUID;

import de.ngloader.common.logger.Logger;
import de.ngloader.common.util.CryptUtil;
import de.ngloader.master.auth.AuthManager;
import de.ngloader.network.EnumProtocolState;
import de.ngloader.network.NetworkManager;
import de.ngloader.network.authentication.INetHandlerAuthenticationServer;
import de.ngloader.network.authentication.client.CPacketAuthenticationChallenge;
import de.ngloader.network.authentication.client.CPacketAuthenticationDisconnect;
import de.ngloader.network.authentication.client.CPacketAuthenticationSuccess;
import de.ngloader.network.authentication.client.CPacketEncryptionRequest;
import de.ngloader.network.authentication.server.SPacketAuthenticationAnswer;
import de.ngloader.network.authentication.server.SPacketAuthenticationStart;
import de.ngloader.network.authentication.server.SPacketEncryptionResponse;

public class NetHandlerAuthenticationServer implements INetHandlerAuthenticationServer {

	private static final KeyPair KEY_PAIR = CryptUtil.generateKeyPair();

	private final NetworkManager networkManager;
	private byte[] token = new byte[4];
	private UUID id;

	public NetHandlerAuthenticationServer(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	public void handleAuthenticationStart(SPacketAuthenticationStart packetAuthenticationStart) {
		this.id = packetAuthenticationStart.getId();
		if(AuthManager.verifyId(this.id)) {
			AuthManager.lockId(this.id);

			CryptUtil.SECURE_RANDOM.nextBytes(token);
			this.networkManager.sendPacket(new CPacketEncryptionRequest(KEY_PAIR.getPublic(), token));
		} else {
			this.disconnect("Invalid or locked id");
			Logger.warn(String.format("Invalid id by %s", this.networkManager.getAddress()));
		}
	}

	@Override
	public void handleEncryptionResponse(SPacketEncryptionResponse packetEncryptionResponse) {
		if(Arrays.equals(this.token, packetEncryptionResponse.getToken(KEY_PAIR.getPrivate()))) {//TODO decrypt slow
			this.networkManager.addEncryptionHandler(packetEncryptionResponse.getSecretKey(KEY_PAIR.getPrivate()));

			if(AuthManager.needsCredentials(this.id)) {
				this.token = new byte[32];
				CryptUtil.SECURE_RANDOM.nextBytes(this.token);
				this.networkManager.sendPacket(new CPacketAuthenticationChallenge(this.token));
			} else {
				this.disconnect("Proxy + Minecraft server not supported yet");
			}
		} else {
			this.disconnect("Invalid token");
			Logger.warn(String.format("Invalid token by %s", this.networkManager.getAddress()));
		}
	}

	@Override
	public void handleAuthenticationAnswer(SPacketAuthenticationAnswer packetAuthenticationAnswer) {
		if(CryptUtil.isSignatureValid(AuthManager.getPublicKey(this.id), this.token, packetAuthenticationAnswer.getToken())) {
			this.networkManager.sendPacket(new CPacketAuthenticationSuccess(8192), future -> this.networkManager.addCompressionHandler(8192));

			//TODO init client instance
			//TODO make own packets

//			this.networkManager.setProtocol(EnumProtocolState.CLOUD);
//			this.networkManager.setNetHandler(new NetHandlerCloudServer(this.networkManager, this.id));
		} else {
			this.disconnect("Invalid signature");
			Logger.warn(String.format("Invalid signature by %s", this.networkManager.getAddress()));
		}
	}

	@Override
	public void onDisconnect(String reason) {
		if(this.id != null)
			AuthManager.unlockId(this.id);
	}

	public void disconnect(String reason) {
		if(this.id != null)
			AuthManager.unlockId(this.id);
		this.networkManager.sendPacket(new CPacketAuthenticationDisconnect(reason), future -> this.networkManager.close(reason));
		this.networkManager.stopReading();
	}
}
