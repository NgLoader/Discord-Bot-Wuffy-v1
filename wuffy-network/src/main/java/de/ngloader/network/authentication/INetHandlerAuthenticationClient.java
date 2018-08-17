package de.ngloader.network.authentication;

import de.ngloader.network.INetHandler;
import de.ngloader.network.authentication.client.CPacketAuthenticationChallenge;
import de.ngloader.network.authentication.client.CPacketAuthenticationDisconnect;
import de.ngloader.network.authentication.client.CPacketAuthenticationSuccess;
import de.ngloader.network.authentication.client.CPacketEncryptionRequest;

/**
 * @author Ingrim4
 */
public interface INetHandlerAuthenticationClient extends INetHandler {

	void handleEncryptionRequest(CPacketEncryptionRequest encryptionRequest);

	void handleAuthenticationChallenge(CPacketAuthenticationChallenge authenticationChallenge);

	void handleAuthenticationSuccess(CPacketAuthenticationSuccess authenticationSuccess);

	void handleAuthenticationDisconnect(CPacketAuthenticationDisconnect authenticationDisconnect);
}