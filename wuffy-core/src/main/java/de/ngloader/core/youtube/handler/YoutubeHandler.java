package de.ngloader.core.youtube.handler;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson2.JacksonFactory;

import de.ngloader.core.youtube.YoutubeAPI;
import de.ngloader.core.youtube.YoutubeOptionalParameterBuilder;
import de.ngloader.core.youtube.YoutubePart;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeHandler {

	protected final YoutubeAPI youtubeAPI;

	protected final OkHttpClient httpClient;

	private String token;

	public YoutubeHandler(YoutubeAPI youtubeAPI, OkHttpClient httpClient, String token) {
		this.youtubeAPI = youtubeAPI;
		this.httpClient = httpClient;
		this.token = token;
	}

	protected <T extends GenericJson> T request(Class<T> requestClass, String url, YoutubePart... parts) {
		try {
			Response response = this.httpClient.newCall(new Request.Builder()
					.url(String.format("%s%s&key=%s",
							url,
							parts.length != 0 ? String.format("&part=%s", Arrays.asList(parts).stream().map(part -> part.getPart()).collect(Collectors.joining(","))) : "",
							this.token))
					.build()).execute();

			return new JacksonFactory().fromString(response.body().string(), requestClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected <T extends GenericJson> T request(Class<T> requestClass, String url, YoutubeOptionalParameterBuilder optionalParametersBuilder, YoutubePart... parts) {
		return this.request(requestClass, url, optionalParametersBuilder.build(), parts);
	}

	protected <T extends GenericJson> T request(Class<T> requestClass, String url, String optionalParameters, YoutubePart... parts) {
		try {
			Response response = this.httpClient.newCall(new Request.Builder()
					.url(String.format("%s%s%s&key=%s",
							url,
							parts.length != 0 ? String.format("&part=%s", Arrays.asList(parts).stream().map(part -> part.getPart()).collect(Collectors.joining(","))) : "",
							optionalParameters,
							this.token))
					.build()).execute();

			return new JacksonFactory().fromString(response.body().string(), requestClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public YoutubeAPI getAPI() {
		return this.youtubeAPI;
	}
}