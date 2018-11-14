package net.wuffy.common.util;

import java.util.Arrays;
import java.util.EnumSet;

public class TimeUtil {

	private EnumSet<EnumDisplayType> displayTypes;

	private long millis;

	private int yearCount = 0;
	private int monthCount = 0;
	private int weekCount = 0;
	private int dayCount = 0;
	private int hourCount = 0;
	private int minCount = 0;
	private int secCount = 0;

	public TimeUtil(long millis, EnumDisplayType... displayTypes) {
		this.millis = millis;
		this.displayTypes = EnumSet.noneOf(EnumDisplayType.class);
		this.displayTypes.addAll(Arrays.asList(displayTypes));
	}

	public TimeUtil(long millis, EnumSet<EnumDisplayType> displayTypes) {
		this.millis = millis;
		this.displayTypes = displayTypes;
	}

	/**
	 * @param Unformated text
	 * @param Milliseconds is %mil
	 * @param Seconds is %sec
	 * @param Minutes is %min
	 * @param Hours is %h
	 * @param Days is %d
	 * @param Weeks is %w
	 * @param Months is %mo
	 * @param Years is %y
	 * 
	 * @param checkZero is when the second, minute, hour, day, week or month is between zero or nine it will automatically adding a zero to the start like (7 to 07)
	 * 
	 * @return Formated text
	 */
	public String format(String text, boolean checkZero) {
		this.calculate();

		return text
				.replace("%mil", Long.toString(millis))
				.replace("%sec", String.format("%s%s", 	!checkZero || secCount 		> 9 ? "" : "0", Integer.toString(secCount)))
				.replace("%min", String.format("%s%s", 	!checkZero || minCount 		> 9 ? "" : "0", Integer.toString(minCount)))
				.replace("%h", String.format("%s%s", 	!checkZero || hourCount 	> 9 ? "" : "0", Integer.toString(hourCount)))
				.replace("%d", String.format("%s%s", 	!checkZero || dayCount 		> 9 ? "" : "0", Integer.toString(dayCount)))
				.replace("%w", String.format("%s%s", 	!checkZero || weekCount 	> 9 ? "" : "0", Integer.toString(weekCount)))
				.replace("%mo", String.format("%s%s", 	!checkZero || monthCount 	> 9 ? "" : "0", Integer.toString(monthCount)))
				.replace("%y", Integer.toString(yearCount));
	}

	public TimeUtil calculate() {
		//Second
		if(this.displayTypes.contains(EnumDisplayType.MILLI)		||
				this.displayTypes.contains(EnumDisplayType.SECOND)	||
				this.displayTypes.contains(EnumDisplayType.MINUTE)	||
				this.displayTypes.contains(EnumDisplayType.HOUR)	||
				this.displayTypes.contains(EnumDisplayType.DAY)		||
				this.displayTypes.contains(EnumDisplayType.WEEK)	||
				this.displayTypes.contains(EnumDisplayType.MONTH)	||
				this.displayTypes.contains(EnumDisplayType.YEAR))
			while(this.millis > 1000) {
				this.millis -= 1000;
				this.secCount++;
			}

		//Minute
		if(this.displayTypes.contains(EnumDisplayType.MINUTE)		||
				this.displayTypes.contains(EnumDisplayType.HOUR)	||
				this.displayTypes.contains(EnumDisplayType.DAY)		||
				this.displayTypes.contains(EnumDisplayType.WEEK)	||
				this.displayTypes.contains(EnumDisplayType.MONTH)	||
				this.displayTypes.contains(EnumDisplayType.YEAR))
			while(this.secCount > 59) {
				this.secCount -= 60;
				this.minCount++;
			}

		//Hour
		if(this.displayTypes.contains(EnumDisplayType.HOUR)			||
				this.displayTypes.contains(EnumDisplayType.DAY)		||
				this.displayTypes.contains(EnumDisplayType.WEEK)	||
				this.displayTypes.contains(EnumDisplayType.MONTH)	||
				this.displayTypes.contains(EnumDisplayType.YEAR))
			while(this.minCount > 59) {
				this.minCount -= 60;
				this.hourCount++;
			}

		//Day
		if(this.displayTypes.contains(EnumDisplayType.DAY)			||
				this.displayTypes.contains(EnumDisplayType.WEEK)	||
				this.displayTypes.contains(EnumDisplayType.MONTH)	||
				this.displayTypes.contains(EnumDisplayType.YEAR))
			while(this.hourCount > 23) {
				this.hourCount -= 24;
				this.dayCount++;
			}

		//Week
		if(this.displayTypes.contains(EnumDisplayType.WEEK)			||
				this.displayTypes.contains(EnumDisplayType.MONTH)	||
				this.displayTypes.contains(EnumDisplayType.YEAR))
			while(this.dayCount > 6) {
				this.dayCount -= 7;
				this.weekCount++;
			}

		//Month
		if(this.displayTypes.contains(EnumDisplayType.MONTH) || this.displayTypes.contains(EnumDisplayType.YEAR))
			while(this.weekCount > 3) {
				this.weekCount -= 4;
				this.monthCount++;
			}

		//Year
		if(this.displayTypes.contains(EnumDisplayType.YEAR))
			while(this.monthCount > 11) {
				this.monthCount -= 12;
				this.yearCount++;
			}

		return this;
	}

	public EnumSet<EnumDisplayType> getDisplayTypes() {
		return this.displayTypes;
	}

	public TimeUtil setDisplayTypes(EnumSet<EnumDisplayType> displayTypes) {
		this.displayTypes = displayTypes;

		return this;
	}

	public long getMillis() {
		return this.millis;
	}

	public TimeUtil setMillis(long millis) {
		this.millis = millis;

		return this;
	}

	public int getYears() {
		return this.yearCount;
	}

	public int getMonths() {
		return this.monthCount;
	}

	public int getWeeks() {
		return this.weekCount;
	}

	public int getDays() {
		return this.dayCount;
	}

	public int getHours() {
		return this.hourCount;
	}

	public int getMinutes() {
		return this.minCount;
	}

	public int getSeconds() {
		return this.secCount;
	}

	public enum EnumDisplayType {
		MILLI,
		SECOND,
		MINUTE,
		HOUR,
		DAY,
		WEEK,
		MONTH,
		YEAR
	}
}