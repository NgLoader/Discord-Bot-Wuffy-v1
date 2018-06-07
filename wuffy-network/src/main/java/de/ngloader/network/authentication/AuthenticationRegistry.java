package de.ngloader.network.authentication;

import de.ngloader.network.PacketRegistry;

public final class AuthenticationRegistry extends PacketRegistry {

	public static final AuthenticationRegistry INSTANCE = new AuthenticationRegistry();

	protected AuthenticationRegistry() {
	}
}