package net.wuffy.bot.module;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;
import net.wuffy.bot.Wuffy;
import net.wuffy.bot.database.DBExtension;
import net.wuffy.common.logger.Logger;

public class ModuleListenerAdapter {

	private static final Map<Long, GuildModule> GUILD_MODULES = new HashMap<Long, GuildModule>();

	private Wuffy core;

	public ModuleListenerAdapter(Wuffy core) {
		this.core = core;
	}

	@SubscribeEvent
	public void onReadyEvent(ReadyEvent event) {
		Logger.info("ShardInitializer", String.format("Initialized shard \"%s\".", Integer.toString(event.getJDA().getShardInfo().getShardId())));
	}

	@SubscribeEvent
	public void onGuildReadyEvent(GuildReadyEvent event) {
		if(!ModuleListenerAdapter.GUILD_MODULES.containsKey(event.getGuild().getIdLong())) {
			GuildModule guildModule = new GuildModule(this.core.getStorageService().getStorage().getProvider(DBExtension.class).getGuild(event.getGuild()));

			ModuleListenerAdapter.GUILD_MODULES.put(event.getGuild().getIdLong(), guildModule);

			Logger.debug("ModuleListenerAdapter", String.format("Initializing guild \"%s\"", Long.valueOf(event.getGuild().getIdLong())));

			if(guildModule.initialize())
				Logger.debug("ModuleListenerAdapter", String.format("Initialized guild \"%s\"", Long.valueOf(event.getGuild().getIdLong())));
			else
				Logger.info("GuildModule", String.format("Guild was already initialized \"%s\"", Long.toString(event.getGuild().getIdLong())));
		}
	}
}