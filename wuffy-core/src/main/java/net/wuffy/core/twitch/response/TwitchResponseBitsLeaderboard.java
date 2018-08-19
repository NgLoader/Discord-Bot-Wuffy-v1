package net.wuffy.core.twitch.response;

@TwitchResponseAnnotation(url = "https://api.twitch.tv/helix/bits/leaderboard")
public class TwitchResponseBitsLeaderboard extends TwitchResponse<TwitchResponseBitsLeaderboard> {

	public String user_id;
	public Integer rank;
	public Integer score;
}