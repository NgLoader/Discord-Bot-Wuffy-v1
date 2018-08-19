package net.wuffy.bot.database;

public class BlockedInfo {
	public Long blockedBy;
	public Long unblockedBy;

	public Long date;
	public Long expire;

	public BlockedInfo(Long blockedBy, Long date, Long expire) {
		this.blockedBy = blockedBy;
		this.date = date;
		this.expire = expire;
	}

	public BlockedInfo(Long blockedBy, Long unblockedBy, Long date, Long expire) {
		this.blockedBy = blockedBy;
		this.unblockedBy = unblockedBy;
		this.date = date;
		this.expire = expire;
	}
}