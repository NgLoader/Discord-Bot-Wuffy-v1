package net.wuffy.bot.module;

import net.wuffy.common.logger.Logger;
import net.wuffy.core.event.CoreListenerAdapter;
import net.wuffy.core.event.EventGuild;
import net.wuffy.core.event.events.WuffyGuildJoinEvent;
import net.wuffy.core.event.events.WuffyGuildLeaveEvent;
import net.wuffy.core.event.events.WuffyReadyEvent;

public class ModuleListenerAdapter extends CoreListenerAdapter<EventGuild> {//TODO call this in JDA instance (onReady event)

	@Override
	public void onReadyEvent(WuffyReadyEvent<EventGuild> event) {
		Logger.info("ShardInitializer", String.format("Initializing shard \"%s\".", Integer.toString(event.getJDA().getShardInfo().getShardId())));

		Logger.info("ShardInitializer", String.format("Initialized shard \"%s\".", Integer.toString(event.getJDA().getShardInfo().getShardId())));
	}

	@Override
	public void onGuildJoinEvent(WuffyGuildJoinEvent<EventGuild> event) {
	}

	@Override
	public void onGuildLeaveEvent(WuffyGuildLeaveEvent<EventGuild> event) {
	}
}