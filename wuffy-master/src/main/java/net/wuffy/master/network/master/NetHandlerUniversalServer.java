package net.wuffy.master.network.master;

import java.util.UUID;

import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.ITickable;
import net.wuffy.master.auth.AuthManager;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.universal.INetHandlerUniversalServer;
import net.wuffy.network.universal.client.CPacketUniversalDisconnect;
import net.wuffy.network.universal.client.CPacketUniversalKeepAlive;
import net.wuffy.network.universal.server.SPacketUniversalKeepAlive;

/**
 * @author Ingirm4
 */
public class NetHandlerUniversalServer implements INetHandlerUniversalServer, ITickable {

	protected final NetworkManager networkManager;
	protected final UUID uuid;

	protected boolean pendingKeepAlive = false;
	protected long keepAliveTimestamp;
	private int ping;

	public NetHandlerUniversalServer(NetworkManager networkManager, UUID uuid) {
		this.networkManager = networkManager;
		this.uuid = uuid;
	}

	@Override
	public void update() {
		long timestamp = System.currentTimeMillis();
		if(timestamp - this.keepAliveTimestamp >= 15000L) {
			if(this.pendingKeepAlive)
				this.disconnect("Timeout");
			else {
				this.pendingKeepAlive = true;
				this.keepAliveTimestamp = timestamp;
				this.networkManager.sendPacket(new CPacketUniversalKeepAlive(this.keepAliveTimestamp));
			}
		}
	}

	@Override
	public void handleKeepAlive(SPacketUniversalKeepAlive keepAlive) {
		if(this.pendingKeepAlive && keepAlive.getTimestamp() == this.keepAliveTimestamp) {
			int offset = (int) (System.currentTimeMillis() - this.keepAliveTimestamp);
			this.ping = (this.ping * 3 + offset) / 4;
			this.pendingKeepAlive = false;
		} else
			this.disconnect("Timeout");
	}

	@Override
	public void onDisconnect(String reason) {
		AuthManager.unlockId(this.uuid);

		Logger.info("NetworkManager", String.format("Disconnected: %s", reason));
	}

	public void disconnect(String reason) {
		this.networkManager.sendPacket(new CPacketUniversalDisconnect(reason), future -> this.networkManager.close(reason));
		this.networkManager.stopReading();
	}

	public int getPing() {
		return this.ping;
	}
}