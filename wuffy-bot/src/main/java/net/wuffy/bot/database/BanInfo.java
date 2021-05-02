package net.wuffy.bot.database;

public class BanInfo {
	public BanType type;

	public Long user;
	public Long banBy;
	public Long unbanBy;

	public Long date;
	public Long expire;

	public String reason;

	public BanInfo(BanType type, Long user, Long banBy, Long date, Long expire, String reason) {
		this.type = type;
		this.user = user;
		this.banBy = banBy;
		this.date = date;
		this.expire = expire;
		this.reason = reason;
	}

	public BanInfo(BanType type, Long user, Long banBy, Long unbanBy, Long date, Long expire, String reason) {
		this.type = type;
		this.user = user;
		this.banBy = banBy;
		this.unbanBy = unbanBy;
		this.date = date;
		this.expire = expire;
		this.reason = reason;
	}

	public enum BanType {
		HARD,
		SOFT,
		NORMAL,
		KICK
	}
}