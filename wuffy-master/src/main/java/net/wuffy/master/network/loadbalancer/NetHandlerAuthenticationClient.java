package net.wuffy.master.network.loadbalancer;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.UUID;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.CryptUtil;
import net.wuffy.master.Master;
import net.wuffy.network.EnumProtocolState;
import net.wuffy.network.IInitNetHandler;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.authentication.INetHandlerAuthenticationClient;
import net.wuffy.network.authentication.client.CPacketAuthenticationChallenge;
import net.wuffy.network.authentication.client.CPacketAuthenticationDisconnect;
import net.wuffy.network.authentication.client.CPacketAuthenticationSuccess;
import net.wuffy.network.authentication.server.SPacketAuthenticationAnswer;
import net.wuffy.network.authentication.server.SPacketAuthenticationStart;

public class NetHandlerAuthenticationClient implements INetHandlerAuthenticationClient, IInitNetHandler {

	private NetworkManager networkManager;

	public NetHandlerAuthenticationClient(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	public void handleAuthenticationChallenge(CPacketAuthenticationChallenge packetAuthenticationChallenge) {
		PrivateKey key = null;

		try (DataInputStream inputStream = new DataInputStream(Files.newInputStream(Paths.get("wuffy/keys.key")))) {
			key = CryptUtil.generatePrivateKey(inputStream.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.networkManager.sendPacket(new SPacketAuthenticationAnswer(
			CryptUtil.generateSignature(key, packetAuthenticationChallenge.getToken())));
	}

	@Override
	public void handleAuthenticationSuccess(CPacketAuthenticationSuccess packetAuthenticationSuccess) {
		this.networkManager.setProtocol(EnumProtocolState.LOADBALANCER);
		this.networkManager.setNetHandler(new NetHandlerLoadBalancerClient(this.networkManager));
	}

	@Override
	public void handleAuthenticationDisconnect(CPacketAuthenticationDisconnect packetAuthenticationDisconnect) {
		this.networkManager.close(packetAuthenticationDisconnect.getReason());
	}

	@Override
	public void onDisconnect(String reason) {
		Logger.info(String.format("Disconnected: %s", reason));
	}

	@Override
	public void onChannelActive() {
		this.networkManager.sendPacket(new SPacketAuthenticationStart(UUID.fromString(Master.getInstance().getConfig().masterId)));
	}
}