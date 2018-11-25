package net.wuffy.core.event.test;

import java.util.ArrayList;

import net.wuffy.common.logger.Logger;

public class EventManager {

	public static void callEvent(Event event) {
		HandlerList handlers = event.getHandlers();
		ArrayList<RegisteredListener> listeners = handlers.getRegisteredListeners();

		for(RegisteredListener registeredListener : listeners)
			try {
				registeredListener.callEvent(event);
			} catch(Throwable e) {
				Logger.fatal("EventManager", String.format("Error by executing event \"%s\"", event.getEventName()), e);
			}
	}
}