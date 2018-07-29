package de.ngloader.bot.jda;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import de.ngloader.bot.WuffyBot;
import de.ngloader.core.Core;
import de.ngloader.core.jda.IJDAAdapter;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.EventListener;

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

			var builder = new DefaultShardManagerBuilder()
					.setToken(this.core.getConfig().token)
					.addEventListeners(this.eventListeners.toArray());

			var shardingConfig = config.sharding;
			if(shardingConfig.enabled)
				builder
					.setShardsTotal(shardingConfig.total)
					.setShards(shardingConfig.shardIds);

			builder.setStatus(config.status);
			builder.setGame(config.game.url != null && !config.game.url.isEmpty() ?
					Game.of(config.game.gameType, config.game.name, config.game.url) :
					Game.of(config.game.gameType, config.game.name));

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