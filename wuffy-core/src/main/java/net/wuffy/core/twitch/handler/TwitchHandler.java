package net.wuffy.core.twitch.handler;

import net.wuffy.common.util.GsonUtil;
import net.wuffy.core.twitch.TwitchAPI;
import net.wuffy.core.twitch.response.TwitchResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TwitchHandler {

	protected final TwitchAPI twitchAPI;

	protected final OkHttpClient httpClient;

	private String clientId;

	public TwitchHandler(TwitchAPI twitchAPI, OkHttpClient httpClient, String clientId) {
		this.twitchAPI = twitchAPI;
		this.httpClient = httpClient;
		this.clientId = clientId;
	}

	protected <T extends TwitchResponse<?>> T request(Class<T> requestClass, String url) {
		try {
			Response response = this.httpClient.newCall(new Request.Builder()
					.header("Client-ID", this.clientId)
					.url(url)
					.build()).execute();

			return GsonUtil.GSON.fromJson(response.body().string(), requestClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public TwitchAPI getAPI() {
		return this.twitchAPI;
	}
}