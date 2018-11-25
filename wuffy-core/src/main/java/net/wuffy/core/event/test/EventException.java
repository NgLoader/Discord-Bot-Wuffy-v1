package net.wuffy.core.event.test;

public class EventException extends Exception {

	private static final long serialVersionUID = -6395133542057991323L;

	private final Throwable throwable;

	public EventException(Throwable throwable) {
		this.throwable = throwable;
	}

	public EventException(Throwable throwable, String message) {
		super(message);

		this.throwable = throwable;
	}

	public Throwable getThrowable() {
		return this.throwable;
	}
}