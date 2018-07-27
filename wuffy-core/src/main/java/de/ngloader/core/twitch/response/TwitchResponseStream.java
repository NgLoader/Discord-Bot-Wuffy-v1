package de.ngloader.core.twitch.response;

@TwitchResponseAnnotation(url = "https://api.twitch.tv/helix/streams")
public class TwitchResponseStream extends TwitchResponse<TwitchResponseStream> {

	public String id;
	public String user_id;
	public String game_id;
//	public List<String> comminity_ids;
	public String type;
	public String title;
	public Integer viewer_count;
	public String started_at;
	public String language;
	public String thumbnail_url;
}