package net.wuffy.network.authentication;

import net.wuffy.network.INetHandler;
import net.wuffy.network.authentication.client.CPacketAuthenticationChallenge;
import net.wuffy.network.authentication.client.CPacketAuthenticationDisconnect;
import net.wuffy.network.authentication.client.CPacketAuthenticationSuccess;

public interface INetHandlerAuthenticationClient extends INetHandler {

	void handleAuthenticationChallenge(CPacketAuthenticationChallenge authenticationChallenge);

	void handleAuthenticationSuccess(CPacketAuthenticationSuccess authenticationSuccess);

	void handleAuthenticationDisconnect(CPacketAuthenticationDisconnect authenticationDisconnect);
}