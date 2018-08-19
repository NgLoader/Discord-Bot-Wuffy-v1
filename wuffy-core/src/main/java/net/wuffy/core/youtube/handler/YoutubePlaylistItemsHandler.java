package net.wuffy.core.youtube.handler;

import com.google.api.services.youtube.model.PlaylistItemListResponse;

import net.wuffy.core.youtube.YoutubeAPI;
import net.wuffy.core.youtube.YoutubeOptionalParameterBuilder;
import net.wuffy.core.youtube.YoutubePart;
import okhttp3.OkHttpClient;

public class YoutubePlaylistItemsHandler extends YoutubeHandler {

	public YoutubePlaylistItemsHandler(YoutubeAPI youtubeAPI, OkHttpClient httpClient, String token) {
		super(youtubeAPI, httpClient, token);
	}

	public PlaylistItemListResponse getByPlaylistId(String userId, YoutubePart... parts) {
		return this.request(PlaylistItemListResponse.class, String.format("%splaylistItems?playlistId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), parts);
	}

	public PlaylistItemListResponse getByPlaylistId(String userId, String optionalParameters, YoutubePart... parts) {
		return this.request(PlaylistItemListResponse.class, String.format("%splaylistItems?playlistId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), optionalParameters, parts);
	}

	public PlaylistItemListResponse getByPlaylistId(String userId, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(PlaylistItemListResponse.class, String.format("%splaylistItems?playlistId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), youtubeOptionalParameterBuilder, parts);
	}

	public PlaylistItemListResponse getById(String userId, YoutubePart... parts) {
		return this.request(PlaylistItemListResponse.class, String.format("%splaylistItems?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), parts);
	}

	public PlaylistItemListResponse getById(String userId, String optionalParameters, YoutubePart... parts) {
		return this.request(PlaylistItemListResponse.class, String.format("%splaylistItems?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), optionalParameters, parts);
	}

	public PlaylistItemListResponse getById(String userId, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(PlaylistItemListResponse.class, String.format("%splaylistItems?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, userId), youtubeOptionalParameterBuilder, parts);
	}
}