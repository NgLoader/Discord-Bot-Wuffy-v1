package net.wuffy.bot.jda;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.wuffy.bot.WuffyBot;
import net.wuffy.core.Core;
import net.wuffy.core.jda.IJDAAdapter;

public class JDAAdapter implements IJDAAdapter {

	private final WuffyBot core;

	private ShardManager shardManager;

	private List<EventListener> eventListeners;

	public JDAAdapter(Core core) {
		this.core = WuffyBot.class.cast(core);
		this.eventListeners = new ArrayList<>();
	}

	@Override
	public void login() {
		if(this.shardManager != null)
			throw new NullPointerException("ShardMananager was not null!");

		try {
			var config = this.core.getConfig();

			var builder = DefaultShardManagerBuilder.createDefault(this.core.getConfig().token);
			builder.addEventListeners(this.eventListeners.toArray());

			var shardingConfig = config.sharding;
			if(shardingConfig.enabled)
				builder
					.setShardsTotal(shardingConfig.total)
					.setShards(shardingConfig.shardIds);

//			builder.setSessionController(controller); //TODO use WuffySessionController

			builder.setStatus(config.status);

			this.shardManager = builder.build();
		} catch (LoginException | IllegalArgumentException e) {
			throw new Error(e);
		}
	}

	@Override
	public void addListener(EventListener listener) {
		if(shardManager != null)
			shardManager.addEventListener(this.eventListeners);
		else if(this.eventListeners != null)
			this.eventListeners.add(listener);
	}

	@Override
	public void logout() {
		if(this.shardManager != null)
			this.shardManager.shutdown();
		this.shardManager = null;
	}

	public void restart() {
		try {
			logout();
		} finally {
			login();
		}
	}

	public ShardManager getShardManager() {
		return shardManager;
	}
}