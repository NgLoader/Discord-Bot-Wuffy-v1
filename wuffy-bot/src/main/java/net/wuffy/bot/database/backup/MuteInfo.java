package net.wuffy.bot.database.backup;

public class MuteInfo {

	public Long user;
	public Long muteBy;
	public Long unmuteBy;

	public Long date;

	public MuteInfo(Long muteBy, Long user, Long date) {
		this.muteBy = muteBy;
		this.user = user;
		this.date = date;
	}

	public MuteInfo(Long muteBy, Long unmuteBy, Long user, Long date) {
		this.muteBy = muteBy;
		this.unmuteBy = unmuteBy;
		this.user = user;
		this.date = date;
	}
}