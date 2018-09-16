package net.wuffy.network.universal;

import net.wuffy.network.INetHandler;
import net.wuffy.network.universal.server.SPacketUniversalKeepAlive;

public interface INetHandlerUniversalServer extends INetHandler {

	void handleKeepAlive(SPacketUniversalKeepAlive keepAlive);
}