package de.ngloader.network.universal;

import de.ngloader.network.INetHandler;
import de.ngloader.network.universal.client.CPacketUniversalDisconnect;
import de.ngloader.network.universal.client.CPacketUniversalKeepAlive;

public interface INetHandlerUniversalClient extends INetHandler {

	void handleKeepAlive(CPacketUniversalKeepAlive keepAlive);

	void handleDisconnect(CPacketUniversalDisconnect disconnect);
}