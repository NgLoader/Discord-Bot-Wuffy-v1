package de.ngloader.core.twitch.response;

public enum EnumTwitchResponse {

	AFTER("after"),
	BEFORE("before"),
	COMMINUTY_ID("comminity_id"),
	FIRST("first"),
	GAME_ID("game_id"),
	LANGUAEG("language"),
	USER_ID("user_id"),
	USER_LOGIN("user_login"),
	ID("id"),
	LOGIN("login"),
	FROM_ID("from_id"),
	TO_ID("to_id");

	public String ending;

	private EnumTwitchResponse(String ending) {
		this.ending = ending;
	}
}