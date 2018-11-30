package net.wuffy.core.event;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

import net.dv8tion.jda.core.hooks.IEventManager;

public class EventManagerAdapter implements IntFunction<IEventManager> {

	private Map<Integer, EventManager> managers = new HashMap<Integer, EventManager>();

	@Override
	public IEventManager apply(int shardId) {
		if(!this.managers.containsKey(shardId))
			this.managers.put(shardId, new EventManager());

		return this.managers.get(shardId);
	}
}