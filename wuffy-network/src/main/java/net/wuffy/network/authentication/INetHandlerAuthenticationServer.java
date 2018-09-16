package net.wuffy.network.authentication;

import net.wuffy.network.INetHandler;
import net.wuffy.network.authentication.server.SPacketAuthenticationAnswer;
import net.wuffy.network.authentication.server.SPacketAuthenticationStart;

public interface INetHandlerAuthenticationServer extends INetHandler {

	void handleAuthenticationStart(SPacketAuthenticationStart authenticationStart);

	void handleAuthenticationAnswer(SPacketAuthenticationAnswer authenticationAnswer);
}