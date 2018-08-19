package net.wuffy.core.twitch.response;

@TwitchResponseAnnotation(url = "https://api.twitch.tv/helix/users/follows")
public class TwitchResponseUsersFollow extends TwitchResponse<TwitchResponseUsersFollow> {

	public String from_id;
	public String to_id;
	public String followed_at;
}