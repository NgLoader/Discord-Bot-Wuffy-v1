package de.ngloader.core.twitch.response;

import java.util.List;

public class TwitchResponse <T extends TwitchResponse<?>> {

	public Integer total;

	public List<T> data;

	public DateRange date_range;

	public Pagination pagination;

	class Pagination {
		public String cursor;
	}

	class DateRange {
		public String started_at;
		public String ended_at;
	}
}