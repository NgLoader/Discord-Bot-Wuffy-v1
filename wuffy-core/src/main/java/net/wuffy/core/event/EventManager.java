package net.wuffy.core.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.IEventManager;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import net.wuffy.common.logger.Logger;

public class EventManager implements IEventManager {

	private final Set<Object> listeners = new HashSet<Object>();
	private final Map<Class<? extends Event>, Map<Object, List<Method>>> methods = new HashMap<Class<? extends Event>, Map<Object, List<Method>>>();

	@Override
	public void register(Object listener) {
		if(this.listeners.add(listener))
			this.updateMethods();
	}

	@Override
	public void unregister(Object listener) {
		if(this.listeners.remove(listener))
			this.updateMethods();
	}

	@Override
	public void handle(Event event) {
		Class<? extends Event> eventClass = event.getClass();

		do {
			Map<Object, List<Method>> listeners = methods.get(eventClass);

			if(listeners != null)
				listeners.entrySet().forEach(e -> e.getValue().forEach(method -> {
					try {
						method.setAccessible(true);
						method.invoke(e.getKey(), event);
					} catch (IllegalAccessException | InvocationTargetException e1) {
						Logger.fatal("EventManager", "Couldn't access annotated eventlistener method", e1);
					} catch (Throwable e2) {
						Logger.fatal("EventManager", "One of the EventListeners had an uncaught exception", e2);
					}
				}));

			eventClass = eventClass == Event.class ? null : (Class<? extends Event>) eventClass.getSuperclass();
		} while(eventClass != null);
	}

	@Override
	public List<Object> getRegisteredListeners() {
		return Collections.unmodifiableList(new ArrayList<Object>(this.listeners));
	}

	private void updateMethods() {
		this.methods.clear();

		for(Object listener : this.listeners) {
			boolean isClass = listener instanceof Class;
			Class<?> clazz = isClass ? (Class<?>) listener : listener.getClass();
			Method[] allMethods = clazz.getDeclaredMethods();

			for(Method method : allMethods) {
				if(!method.isAnnotationPresent(SubscribeEvent.class) || (isClass && !Modifier.isStatic(method.getModifiers())))
					continue;

				Class<?>[] parameterTypes = method.getParameterTypes();

				if(parameterTypes.length == 1 && Event.class.isAssignableFrom(parameterTypes[0])) {
					Class<? extends Event> eventClass = (Class<? extends Event>) parameterTypes[0];

					if(!this.methods.containsKey(eventClass))
						this.methods.put(eventClass, new HashMap<Object, List<Method>>());
					if(!this.methods.get(eventClass).containsKey(listener))
						this.methods.get(eventClass).put(listener, new ArrayList<Method>());

					this.methods.get(eventClass).get(listener).add(method);
				}
			}
		}
	}
}