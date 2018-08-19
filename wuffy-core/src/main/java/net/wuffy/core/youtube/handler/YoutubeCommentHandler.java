package net.wuffy.core.youtube.handler;

import com.google.api.services.youtube.model.CommentListResponse;

import net.wuffy.core.youtube.YoutubeAPI;
import net.wuffy.core.youtube.YoutubeOptionalParameterBuilder;
import net.wuffy.core.youtube.YoutubePart;
import okhttp3.OkHttpClient;

public class YoutubeCommentHandler extends YoutubeHandler {

	public YoutubeCommentHandler(YoutubeAPI youtubeAPI, OkHttpClient httpClient, String token) {
		super(youtubeAPI, httpClient, token);
	}

	public CommentListResponse getByParentId(String parentId, YoutubePart... parts) {
		return this.request(CommentListResponse.class, String.format("%CommentListResponse?parentId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, parentId), parts);
	}

	public CommentListResponse getByParentId(String parentId, String optionalParameters, YoutubePart... parts) {
		return this.request(CommentListResponse.class, String.format("%CommentListResponse?parentId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, parentId), optionalParameters, parts);
	}

	public CommentListResponse getByParentId(String parentId, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(CommentListResponse.class, String.format("%CommentListResponse?parentId=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, parentId), youtubeOptionalParameterBuilder, parts);
	}

	public CommentListResponse getById(String id, YoutubePart... parts) {
		return this.request(CommentListResponse.class, String.format("%CommentListResponse?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, id), parts);
	}

	public CommentListResponse getById(String id, String optionalParameters, YoutubePart... parts) {
		return this.request(CommentListResponse.class, String.format("%CommentListResponse?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, id), optionalParameters, parts);
	}

	public CommentListResponse getById(String id, YoutubeOptionalParameterBuilder youtubeOptionalParameterBuilder, YoutubePart... parts) {
		return this.request(CommentListResponse.class, String.format("%CommentListResponse?id=%s", YoutubeAPI.YOUTUBE_REQUEST_URL, id), youtubeOptionalParameterBuilder, parts);
	}
}