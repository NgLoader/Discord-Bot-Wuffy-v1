package net.wuffy.core.twitch.response;

@TwitchResponseAnnotation(url = "https://api.twitch.tv/helix/users")
public class TwitchResponseUser extends TwitchResponse<TwitchResponseUser> {

	public String id;
	public String login;
	public String display_name;
	public String type;
	public String boradcaster_type;
	public String description;
	public String profile_image_url;
	public String offline_image_url;
	public Integer view_count;
	public String email;
}