package net.wuffy.network.universal;

import net.wuffy.network.INetHandler;
import net.wuffy.network.universal.client.CPacketUniversalDisconnect;
import net.wuffy.network.universal.client.CPacketUniversalKeepAlive;

public interface INetHandlerUniversalClient extends INetHandler {

	void handleKeepAlive(CPacketUniversalKeepAlive keepAlive);

	void handleDisconnect(CPacketUniversalDisconnect disconnect);
}