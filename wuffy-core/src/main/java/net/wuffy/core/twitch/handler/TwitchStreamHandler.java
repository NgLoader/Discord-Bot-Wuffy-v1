package net.wuffy.core.twitch.handler;

import java.util.List;

import net.wuffy.core.twitch.TwitchAPI;
import net.wuffy.core.twitch.response.TwitchResponse;
import net.wuffy.core.twitch.response.TwitchResponseStream;
import okhttp3.OkHttpClient;

public class TwitchStreamHandler extends TwitchHandler {

	public TwitchStreamHandler(TwitchAPI twitchAPI, OkHttpClient httpClient, String clientId) {
		super(twitchAPI, httpClient, clientId);
	}

	public TwitchResponse<TwitchResponseStream> getById(String userId) {
		return this.request(TwitchResponseStream.class, String.format("%sstreams?user_id=%s", TwitchAPI.TWITCH_REQUEST_URL, userId));
	}

	public TwitchResponse<TwitchResponseStream> getById(List<String> userIds) {
		return this.request(TwitchResponseStream.class, String.format("%sstreams?user_id=%s", TwitchAPI.TWITCH_REQUEST_URL, String.join("&user_id=", userIds)));
	}
}