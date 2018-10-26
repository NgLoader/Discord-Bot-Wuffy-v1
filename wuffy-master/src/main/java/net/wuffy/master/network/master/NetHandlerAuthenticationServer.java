package net.wuffy.master.network.master;

import java.util.UUID;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.CryptUtil;
import net.wuffy.master.auth.AuthManager;
import net.wuffy.network.EnumProtocolState;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.authentication.INetHandlerAuthenticationServer;
import net.wuffy.network.authentication.client.CPacketAuthenticationChallenge;
import net.wuffy.network.authentication.client.CPacketAuthenticationDisconnect;
import net.wuffy.network.authentication.client.CPacketAuthenticationSuccess;
import net.wuffy.network.authentication.server.SPacketAuthenticationAnswer;
import net.wuffy.network.authentication.server.SPacketAuthenticationStart;

public class NetHandlerAuthenticationServer implements INetHandlerAuthenticationServer {

	private final NetworkManager networkManager;
	private byte[] token = new byte[32];
	private UUID uuid;

	public NetHandlerAuthenticationServer(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	public void handleAuthenticationStart(SPacketAuthenticationStart packetAuthenticationStart) {
		this.uuid = packetAuthenticationStart.getId();

		if(AuthManager.verifyId(this.uuid)) {
			if(AuthManager.getName(this.uuid) != null) {
				AuthManager.lockId(this.uuid);

				CryptUtil.SECURE_RANDOM.nextBytes(this.token);
				this.networkManager.sendPacket(new CPacketAuthenticationChallenge(this.token));
			} else {
				this.disconnect("No name found");
				Logger.warn("NetworkHandler (AuthenticationServer)", String.format("No name found for id %s", this.uuid.toString()));
			}
		} else {
			this.disconnect("Invalid or locked id");
			Logger.warn("NetworkHandler (AuthenticationServer)", String.format("Invalid id by %s", this.networkManager.getAddress()));
		}
	}

	@Override
	public void handleAuthenticationAnswer(SPacketAuthenticationAnswer packetAuthenticationAnswer) {
		if(CryptUtil.isSignatureValid(AuthManager.getPublicKey(this.uuid), this.token, packetAuthenticationAnswer.getToken())) {
			this.networkManager.sendPacket(new CPacketAuthenticationSuccess());

			this.networkManager.setProtocol(EnumProtocolState.MASTER);
			this.networkManager.setNetHandler(new NetHandlerMasterServer(this.networkManager, uuid));
		} else {
			this.disconnect("Invalid signature");
			Logger.warn("NetworkHandler (AuthenticationServer)", String.format("Invalid signature by %s", this.networkManager.getAddress()));
		}
	}

	@Override
	public void onDisconnect(String reason) {
		if(this.uuid != null)
			AuthManager.unlockId(this.uuid);
	}

	public void disconnect(String reason) {
		if(this.uuid != null)
			AuthManager.unlockId(this.uuid);

		this.networkManager.sendPacket(new CPacketAuthenticationDisconnect(reason), future -> this.networkManager.close(reason));
		this.networkManager.stopReading();
	}
}