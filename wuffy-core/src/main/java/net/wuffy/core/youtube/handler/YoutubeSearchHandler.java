package net.wuffy.core.youtube.handler;

import com.google.api.services.youtube.model.SearchListResponse;

import net.wuffy.core.youtube.YoutubeAPI;
import net.wuffy.core.youtube.YoutubeOptionalParameterBuilder;
import net.wuffy.core.youtube.YoutubePart;
import okhttp3.OkHttpClient;

public class YoutubeSearchHandler extends YoutubeHandler {

	public YoutubeSearchHandler(YoutubeAPI youtubeAPI, OkHttpClient httpClient, String token) {
		super(youtubeAPI, httpClient, token);
	}

	public SearchListResponse getByQ(String search, String optionalParameters, YoutubePart... parts) {
		return this.request(SearchListResponse.class, String.format("%ssearch?q=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, search.replace(" ", "%20")), optionalParameters, parts);
	}

	public SearchListResponse getByQ(String search, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(SearchListResponse.class, String.format("%ssearch?q=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, search.replace(" ", "%20")), youtubeOptionalParameterBuilder, parts);
	}

	public SearchListResponse get(String optionalParameters, YoutubePart... parts) {
		return this.request(SearchListResponse.class, String.format("%ssearch?", YoutubeAPI.YOUTUBE_REQUEST_URL), optionalParameters, parts);
	}

	public SearchListResponse get(YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(SearchListResponse.class, String.format("%ssearch?", YoutubeAPI.YOUTUBE_REQUEST_URL), youtubeOptionalParameterBuilder, parts);
	}
}