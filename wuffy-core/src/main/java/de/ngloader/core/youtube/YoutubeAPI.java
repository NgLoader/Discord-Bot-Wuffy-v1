package de.ngloader.core.youtube;

import de.ngloader.core.youtube.handler.YoutubeChannelHandler;
import de.ngloader.core.youtube.handler.YoutubeCommentHandler;
import de.ngloader.core.youtube.handler.YoutubePlaylistItemsHandler;
import de.ngloader.core.youtube.handler.YoutubeSearchHandler;
import de.ngloader.core.youtube.handler.YoutubeVideoHandler;
import okhttp3.OkHttpClient;

public class YoutubeAPI {

	public static final String YOUTUBE_REQUEST_URL = "https://www.googleapis.com/youtube/v3/";

	private final OkHttpClient httpClient = new OkHttpClient();

	private final String token;

	private final YoutubePlaylistItemsHandler playlistItemsHandler;
	private final YoutubeChannelHandler channelHandler;
	private final YoutubeCommentHandler commentHandler;
	private final YoutubeSearchHandler searchHandler;
	private final YoutubeVideoHandler videoHandler;

	public YoutubeAPI(String token) {
		this.token = token;

		this.playlistItemsHandler = new YoutubePlaylistItemsHandler(this, this.httpClient, this.token);
		this.channelHandler = new YoutubeChannelHandler(this, this.httpClient, this.token);
		this.commentHandler = new YoutubeCommentHandler(this, this.httpClient, this.token);
		this.searchHandler = new YoutubeSearchHandler(this, this.httpClient, this.token);
		this.videoHandler = new YoutubeVideoHandler(this, this.httpClient, this.token);
	}

	public YoutubePlaylistItemsHandler getPlaylistItemsHandler() {
		return this.playlistItemsHandler;
	}

	public YoutubeChannelHandler getChannelHandler() {
		return this.channelHandler;
	}

	public YoutubeCommentHandler getCommentHandler() {
		return this.commentHandler;
	}

	public YoutubeSearchHandler getSearchHandler() {
		return this.searchHandler;
	}

	public YoutubeVideoHandler getVideoHandler() {
		return this.videoHandler;
	}
}
