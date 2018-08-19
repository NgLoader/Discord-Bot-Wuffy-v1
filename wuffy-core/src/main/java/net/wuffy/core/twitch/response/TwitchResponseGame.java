package net.wuffy.core.twitch.response;

@TwitchResponseAnnotation(url = "https://api.twitch.tv/helix/games")
public class TwitchResponseGame extends TwitchResponse<TwitchResponseGame> {

	public String id;
	public String name;
	public String box_art_url;
}