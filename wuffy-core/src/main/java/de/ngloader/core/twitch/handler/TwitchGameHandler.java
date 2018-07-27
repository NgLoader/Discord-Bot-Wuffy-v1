package de.ngloader.core.twitch.handler;

import de.ngloader.core.twitch.TwitchAPI;
import okhttp3.OkHttpClient;

public class TwitchGameHandler extends TwitchHandler {

	public TwitchGameHandler(TwitchAPI twitchAPI, OkHttpClient httpClient, String clientId) {
		super(twitchAPI, httpClient, clientId);
	}
}