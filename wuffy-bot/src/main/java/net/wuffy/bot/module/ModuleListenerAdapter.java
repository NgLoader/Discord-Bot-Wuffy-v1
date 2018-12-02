package net.wuffy.bot.module;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildUnavailableEvent;
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
		Logger.info("ShardInitializer", String.format("Shard \"%s\" is ready", Integer.toString(event.getJDA().getShardInfo().getShardId())));
	}

	@SubscribeEvent
	public void onGuildReadyEvent(GuildReadyEvent event) {
		this.handleGuildReady(event.getGuild());
	}

	@SubscribeEvent
	public void onGuildAvailableEvent(GuildAvailableEvent event) {
		this.handleGuildReady(event.getGuild());
	}

	@SubscribeEvent
	public void onGuildUnavailableEvent(GuildUnavailableEvent event) {
		this.handleGuildDestoryed(event.getGuild());
	}

	@SubscribeEvent
	public void onGuildLeaveEvent(GuildLeaveEvent event) {
		this.handleGuildDestoryed(event.getGuild());

//		this.core.getStorageService().getStorage().getProvider(DBExtension.class).getGuild(event.getGuild()).deleteFromDatabase(); NOT USED (Database auto delete)
	}

	public void handleGuildReady(Guild guild) {
		if(guild == null)
			return;

		Long guildId = guild.getIdLong();

		if(!ModuleListenerAdapter.GUILD_MODULES.containsKey(guildId)) {
			GuildModule guildModule = new GuildModule(this.core.getStorageService().getStorage().getProvider(DBExtension.class).getGuild(guild));

			ModuleListenerAdapter.GUILD_MODULES.put(guildId, guildModule);

			Logger.debug("ModuleListenerAdapter", String.format("Initializing guild \"%s\"", Long.valueOf(guildId)));

			if(guildModule.initialize())
				Logger.debug("ModuleListenerAdapter", String.format("Initialized guild \"%s\"", Long.valueOf(guildId)));
			else
				Logger.info("GuildModule", String.format("Guild was already initialized \"%s\"", Long.toString(guildId)));
		}
	}

	public void handleGuildDestoryed(Guild guild) {
		if(guild == null)
			return;

		Long guildId = guild.getIdLong();

		if(ModuleListenerAdapter.GUILD_MODULES.containsKey(guildId)) {
			GuildModule guildModule = ModuleListenerAdapter.GUILD_MODULES.remove(guildId);

			if(guildModule != null) {
				guildModule.destroy();
				Logger.debug("GuildModule", String.format("Destroyed guild \"%s\"", Long.valueOf(guildId)));
			}
		}
	}
}