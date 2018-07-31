package de.ngloader.core.youtube.handler;

import com.google.api.services.youtube.model.ChannelListResponse;

import de.ngloader.core.youtube.YoutubeAPI;
import de.ngloader.core.youtube.YoutubeOptionalParameterBuilder;
import de.ngloader.core.youtube.YoutubePart;
import okhttp3.OkHttpClient;

public class YoutubeChannelHandler extends YoutubeHandler {

	public YoutubeChannelHandler(YoutubeAPI youtubeAPI, OkHttpClient httpClient, String token) {
		super(youtubeAPI, httpClient, token);
	}

	public ChannelListResponse getByCategoryId(String userId, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?categoryId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), parts);
	}

	public ChannelListResponse getByCategoryId(String userId, String optionalParameters, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?categoryId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), optionalParameters, parts);
	}

	public ChannelListResponse getByCategoryId(String userId, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?categoryId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), youtubeOptionalParameterBuilder, parts);
	}

	public ChannelListResponse getByUsername(String username, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?forUsername=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, username), parts);
	}

	public ChannelListResponse getByUsername(String username, String optionalParameters, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?forUsername=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, username), optionalParameters, parts);
	}

	public ChannelListResponse getByUsername(String username, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?forUsername=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, username), youtubeOptionalParameterBuilder, parts);
	}

	public ChannelListResponse getById(String userId, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), parts);
	}

	public ChannelListResponse getById(String userId, String optionalParameters, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), optionalParameters, parts);
	}

	public ChannelListResponse getById(String userId, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), youtubeOptionalParameterBuilder, parts);
	}

	public ChannelListResponse getBy(String by, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?%s", YoutubeAPI.YOUTUBE_REQUEST_URL, by), parts);
	}

	public ChannelListResponse getBy(String by, String optionalParameters, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?%s", YoutubeAPI.YOUTUBE_REQUEST_URL, by), optionalParameters, parts);
	}

	public ChannelListResponse getBy(String by, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?%s", YoutubeAPI.YOUTUBE_REQUEST_URL, by), youtubeOptionalParameterBuilder, parts);
	}

	public ChannelListResponse get(String optionalParameters, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?", YoutubeAPI.YOUTUBE_REQUEST_URL), optionalParameters, parts);
	}

	public ChannelListResponse get(YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(ChannelListResponse.class, String.format("%schannels?", YoutubeAPI.YOUTUBE_REQUEST_URL), youtubeOptionalParameterBuilder, parts);
	}
}