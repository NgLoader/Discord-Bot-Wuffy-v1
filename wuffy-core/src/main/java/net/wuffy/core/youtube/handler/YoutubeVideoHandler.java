package net.wuffy.core.youtube.handler;

import com.google.api.services.youtube.model.VideoListResponse;

import net.wuffy.core.youtube.YoutubeAPI;
import net.wuffy.core.youtube.YoutubeOptionalParameterBuilder;
import net.wuffy.core.youtube.YoutubePart;
import okhttp3.OkHttpClient;

public class YoutubeVideoHandler extends YoutubeHandler {

	public YoutubeVideoHandler(YoutubeAPI youtubeAPI, OkHttpClient httpClient, String token) {
		super(youtubeAPI, httpClient, token);
	}

	public VideoListResponse getByChart(String userId, YoutubePart... parts) {
		return this.request(VideoListResponse.class, String.format("%svideos?chart=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), parts);
	}

	public VideoListResponse getByChart(String userId, String optionalParameters, YoutubePart... parts) {
		return this.request(VideoListResponse.class, String.format("%svideos?chart=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), optionalParameters, parts);
	}

	public VideoListResponse getByChart(String userId, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(VideoListResponse.class, String.format("%svideos?chart=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), youtubeOptionalParameterBuilder, parts);
	}

	public VideoListResponse getById(String userId, YoutubePart... parts) {
		return this.request(VideoListResponse.class, String.format("%svideos?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), parts);
	}

	public VideoListResponse getById(String userId, String optionalParameters, YoutubePart... parts) {
		return this.request(VideoListResponse.class, String.format("%svideos?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), optionalParameters, parts);
	}

	public VideoListResponse getById(String userId, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(VideoListResponse.class, String.format("%svideos?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), youtubeOptionalParameterBuilder, parts);
	}
}