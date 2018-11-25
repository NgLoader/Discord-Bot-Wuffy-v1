package net.wuffy.core.event.test;

public interface EventExecuter {

	public void execute(Listener listener, Event event) throws EventException;
}