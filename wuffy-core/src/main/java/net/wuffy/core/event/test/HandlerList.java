package net.wuffy.core.event.test;

import java.util.ArrayList;
import java.util.ListIterator;

public class HandlerList {

	private final ArrayList<RegisteredListener> handlers = new ArrayList<RegisteredListener>();

	public synchronized void unregister(Listener listener) {
		for(ListIterator<RegisteredListener> iterator = this.handlers.listIterator(); iterator.hasNext();)
			if(iterator.next().getListener().equals(listener))
				iterator.remove();
	}

	public synchronized void register(RegisteredListener listener) {
		if(this.handlers.contains(listener))
			throw new IllegalStateException(String.format("This listener is already registred (%s)", listener.getClass().getSimpleName()));

		this.handlers.add(listener);
	}

	public ArrayList<RegisteredListener> getRegisteredListeners() {
		return this.handlers;
	}
}