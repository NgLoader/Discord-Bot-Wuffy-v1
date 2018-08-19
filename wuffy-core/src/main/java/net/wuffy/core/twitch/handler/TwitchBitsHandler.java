package net.wuffy.core.twitch.handler;

import net.wuffy.core.twitch.TwitchAPI;
import okhttp3.OkHttpClient;

public class TwitchBitsHandler extends TwitchHandler {

	public TwitchBitsHandler(TwitchAPI twitchAPI, OkHttpClient httpClient, String clientId) {
		super(twitchAPI, httpClient, clientId);
	}
}