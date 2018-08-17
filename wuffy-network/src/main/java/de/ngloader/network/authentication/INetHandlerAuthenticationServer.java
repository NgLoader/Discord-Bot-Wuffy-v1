package de.ngloader.network.authentication;

import de.ngloader.network.INetHandler;
import de.ngloader.network.authentication.server.SPacketAuthenticationAnswer;
import de.ngloader.network.authentication.server.SPacketAuthenticationStart;
import de.ngloader.network.authentication.server.SPacketEncryptionResponse;

/**
 * @author Ingrim4
 */
public interface INetHandlerAuthenticationServer extends INetHandler {

	void handleAuthenticationStart(SPacketAuthenticationStart authenticationStart);

	void handleEncryptionResponse(SPacketEncryptionResponse encryptionResponse);

	void handleAuthenticationAnswer(SPacketAuthenticationAnswer authenticationAnswer);
}