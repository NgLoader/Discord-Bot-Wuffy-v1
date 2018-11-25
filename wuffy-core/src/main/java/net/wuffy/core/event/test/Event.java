package net.wuffy.core.event.test;

public abstract class Event {

	private String name;

	public String getEventName() {
		if(name == null)
			return this.getClass().getSimpleName();

		return name;
	}

	public abstract HandlerList getHandlers();
}