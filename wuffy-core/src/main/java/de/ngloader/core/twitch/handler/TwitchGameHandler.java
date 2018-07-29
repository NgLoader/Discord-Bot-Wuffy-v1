package de.ngloader.core.twitch.handler;

import java.util.List;

import de.ngloader.core.twitch.TwitchAPI;
import de.ngloader.core.twitch.response.TwitchResponse;
import de.ngloader.core.twitch.response.TwitchResponseGame;
import okhttp3.OkHttpClient;

public class TwitchGameHandler extends TwitchHandler {

	public TwitchGameHandler(TwitchAPI twitchAPI, OkHttpClient httpClient, String clientId) {
		super(twitchAPI, httpClient, clientId);
	}

	public TwitchResponse<TwitchResponseGame> getById(String id) {
		return this.request(TwitchResponseGame.class, String.format("%sgames?id=%s", TwitchAPI.TWITCH_REQUEST_URL, id));
	}

	public TwitchResponse<TwitchResponseGame> getById(List<String> ids) {
		return this.request(TwitchResponseGame.class, String.format("%sgames?id=%s", TwitchAPI.TWITCH_REQUEST_URL, String.join("&id=", ids)));
	}

	public TwitchResponse<TwitchResponseGame> getByName(String name) {
		return this.request(TwitchResponseGame.class, String.format("%sgames?name=%s", TwitchAPI.TWITCH_REQUEST_URL, name));
	}

	public TwitchResponse<TwitchResponseGame> getByName(List<String> names) {
		return this.request(TwitchResponseGame.class, String.format("%sgames?name=%s", TwitchAPI.TWITCH_REQUEST_URL, String.join("&name=", names)));
	}
}