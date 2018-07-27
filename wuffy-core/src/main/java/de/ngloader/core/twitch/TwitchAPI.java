package de.ngloader.core.twitch;

import de.ngloader.core.twitch.handler.TwitchBitsHandler;
import de.ngloader.core.twitch.handler.TwitchGameHandler;
import de.ngloader.core.twitch.handler.TwitchStreamHandler;
import de.ngloader.core.twitch.handler.TwitchUserHandler;
import okhttp3.OkHttpClient;

public class TwitchAPI {

	public static final String TWITCH_REQUEST_URL = "https://api.twitch.tv/helix/";

	private final OkHttpClient httpClient = new OkHttpClient();

	private final String clientId;

	private final TwitchBitsHandler bitsHandler;
	private final TwitchGameHandler gameHandler;
	private final TwitchStreamHandler streamHandler;
	private final TwitchUserHandler userHandler;

	public TwitchAPI(String clientId) {
		this.clientId = clientId;

		this.bitsHandler = new TwitchBitsHandler(this, this.httpClient, this.clientId);
		this.gameHandler = new TwitchGameHandler(this, this.httpClient, this.clientId);
		this.streamHandler = new TwitchStreamHandler(this, this.httpClient, this.clientId);
		this.userHandler = new TwitchUserHandler(this, this.httpClient, this.clientId);
	}

	public TwitchBitsHandler getBitsHandler() {
		return this.bitsHandler;
	}

	public TwitchGameHandler getGameHandler() {
		return this.gameHandler;
	}

	public TwitchStreamHandler getStreamHandler() {
		return this.streamHandler;
	}

	public TwitchUserHandler getUserHandler() {
		return this.userHandler;
	}
}