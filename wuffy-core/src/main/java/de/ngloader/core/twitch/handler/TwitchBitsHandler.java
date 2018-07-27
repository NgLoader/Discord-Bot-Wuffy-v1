package de.ngloader.core.twitch.handler;

import de.ngloader.core.twitch.TwitchAPI;
import okhttp3.OkHttpClient;

public class TwitchBitsHandler extends TwitchHandler {

	public TwitchBitsHandler(TwitchAPI twitchAPI, OkHttpClient httpClient, String clientId) {
		super(twitchAPI, httpClient, clientId);
	}
}