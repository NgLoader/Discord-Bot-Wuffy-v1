package net.wuffy.master.network.loadbalancer;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.UUID;
import java.util.stream.Stream;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.CryptUtil;
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

	private UUID id;
	private PrivateKey privateKey;

	public NetHandlerAuthenticationClient(NetworkManager networkManager) {
		this.networkManager = networkManager;

		Path keyPath = Paths.get("wuffy");

		if(Files.exists(keyPath))
			try (Stream<Path> paths = Files.walk(keyPath)) {
				paths.filter(path -> Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS))
					.forEach(path -> {
						if(path.getFileName().toString().endsWith(".key"))
							try (DataInputStream inputStream = new DataInputStream(Files.newInputStream(path))) {
								this.id = new UUID(inputStream.readLong(), inputStream.readLong());
								this.privateKey = CryptUtil.generatePrivateKey(inputStream.readAllBytes());
	
								Logger.info("NetworkHandler", String.format("Loaded Id \"%s\" with privateKey.", this.id.toString()));
								return;
							} catch (IOException e) {
								Logger.fatal("NetworkHandler", String.format("Error by loading logindata. (Path: \"%s\")", path.toString()), e);
							}
					});
			} catch (IOException e) {
				Logger.fatal("AuthManager", "Failed by initializing.", e);
			}

		if(this.id == null || this.privateKey == null) {
			this.networkManager.close("Logindata not found.\nPlease create logindata with the command \"master generate\" in LoadBalancer.");
			Logger.err("NetworkHandler", "Logindata not found.\nPlease create logindata with the command \"master generate\" in LoadBalancer.");
		}
	}

	@Override
	public void onChannelActive() {
		this.networkManager.sendPacket(new SPacketAuthenticationStart(this.id));
	}

	@Override
	public void handleAuthenticationChallenge(CPacketAuthenticationChallenge packetAuthenticationChallenge) {
		this.networkManager.sendPacket(new SPacketAuthenticationAnswer(CryptUtil.generateSignature(this.privateKey, packetAuthenticationChallenge.getToken())));
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
}