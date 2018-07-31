package de.ngloader.core.twitch.handler;

import java.util.List;

import de.ngloader.core.twitch.TwitchAPI;
import de.ngloader.core.twitch.response.TwitchResponse;
import de.ngloader.core.twitch.response.TwitchResponseUser;
import okhttp3.OkHttpClient;

public class TwitchUserHandler extends TwitchHandler {

	public TwitchUserHandler(TwitchAPI twitchAPI, OkHttpClient httpClient, String clientId) {
		super(twitchAPI, httpClient, clientId);
	}

	public TwitchResponse<TwitchResponseUser> getByName(String userName) {
		return this.request(TwitchResponseUser.class, String.format("%susers?login=%s", TwitchAPI.TWITCH_REQUEST_URL, userName));
	}

	public TwitchResponse<TwitchResponseUser> getByName(List<String> userNames) {
		return this.request(TwitchResponseUser.class, String.format("%susers?login=%s", TwitchAPI.TWITCH_REQUEST_URL, String.join("&login=", userNames)));
	}

	public TwitchResponse<TwitchResponseUser> getById(String id) {
		return this.request(TwitchResponseUser.class, String.format("%susers?id=%s", TwitchAPI.TWITCH_REQUEST_URL, id));
	}

	public TwitchResponse<TwitchResponseUser> getById(List<String> ids) {
		return this.request(TwitchResponseUser.class, String.format("%susers?id=%s", TwitchAPI.TWITCH_REQUEST_URL, String.join("&id=", ids)));
	}

	public TwitchResponse<TwitchResponseUser> getByNameAndId(List<String> userNames, List<String> ids) {
		return this.request(TwitchResponseUser.class, String.format("%susers?%s%s",
				TwitchAPI.TWITCH_REQUEST_URL,
				((userNames.isEmpty() ? "" : "login=") + String.join("&login=", userNames)),
				((userNames.isEmpty() ? ids.isEmpty() ? "" : "id="  : ids.isEmpty() ? "" : "&id=") + String.join("&id=", ids))));
	}
}