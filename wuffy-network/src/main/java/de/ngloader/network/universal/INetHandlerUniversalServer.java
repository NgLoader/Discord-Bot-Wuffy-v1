package de.ngloader.network.universal;

import de.ngloader.network.INetHandler;
import de.ngloader.network.universal.server.SPacketUniversalKeepAlive;

public interface INetHandlerUniversalServer extends INetHandler {

	void handleKeepAlive(SPacketUniversalKeepAlive keepAlive);
}