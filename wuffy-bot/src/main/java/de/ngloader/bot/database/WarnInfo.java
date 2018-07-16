package de.ngloader.bot.database;

public class WarnInfo {
	public Long user;
	public Long warnBy;

	public Long date;
	public Long expire;

	public Integer pointsHas;
	public Integer pointsAdded;

	public WarnInfo(Long user, Long warnBy, Long date, Long expire, Integer pointsHas, Integer pointsAdded) {
		this.user = user;
		this.warnBy = warnBy;
		this.date = date;
		this.expire = expire;
		this.pointsHas = pointsHas;
		this.pointsAdded = pointsAdded;
	}
}