package net.wuffy.bot.jda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.hooks.EventListener;
import net.wuffy.bot.WuffyBot;
import net.wuffy.common.logger.Logger;
import net.wuffy.core.Core;
import net.wuffy.core.jda.IJDA;
import net.wuffy.network.bot.client.CPacketBotSettings;
import net.wuffy.network.bot.client.CPacketBotSettings.EnumMasterSettings;
import net.wuffy.network.bot.client.CPacketBotSettings.GatewayBot;
import net.wuffy.network.bot.client.CPacketBotSettings.Status;
import net.wuffy.network.bot.client.CPacketBotShardUpdate;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class JDAAdapter implements IJDA {

	private final WuffyBot core;

	private ShardManager shardManager;

	private GatewayBot gatewayBot;
	private Status status;

	private List<Integer> shardIds = new ArrayList<Integer>();
	private int shardsTotal = 0;

	public JDAAdapter(Core core, CPacketBotSettings packetBotSettings) {
		this.core = WuffyBot.class.cast(core);
	}

	public void handleSettings(CPacketBotSettings packetBotSettings) {
		//GatewayBot
		if(packetBotSettings.isContainsType(EnumMasterSettings.GATEWAYBOT)) {
			this.gatewayBot = packetBotSettings.getGatewayBot();

			if(this.shardManager != null)
				this.restart();
		}

		//Status
		if(packetBotSettings.isContainsType(EnumMasterSettings.STATUS)) {
			if(this.shardManager != null) {
				this.shardManager.setStatusProvider(id -> this.status.getStatusType(OnlineStatus.class));
				this.shardManager.setGameProvider(id -> this.status.getGameUrl() != null && Game.isValidStreamingUrl(this.status.getGameUrl()) ?
						Game.of(this.status.getGameType(GameType.class), this.status.getGameName(), this.status.getGameName()) :
						Game.of(this.status.getGameType(GameType.class), this.status.getGameName()));
			}

			this.status = packetBotSettings.getStatus();
		}
	}

	public void handleShardUpdate(CPacketBotShardUpdate packetBotShardUpdate) {
		switch (packetBotShardUpdate.getType()) {
		case START:
			this.shardIds.add(packetBotShardUpdate.getValue());

			if(this.shardManager == null && this.shardsTotal != 0 && !this.shardIds.isEmpty())
				this.login();
			break;

		case STOP:
			this.shardIds.remove(packetBotShardUpdate.getValue());

			if(this.shardManager != null)
				if(this.shardIds.isEmpty())
					this.logout();
				else
					this.shardManager.shutdown(packetBotShardUpdate.getValue());
			break;

		case SHARDCOUNT:
			try {
				if(this.shardManager != null)
					this.logout();
			} finally {
				this.shardsTotal = packetBotShardUpdate.getValue();

				this.login();
			}
			break;

		default:
			break;
		}
	}

	public DefaultShardManagerBuilder buildShardManagerBuilder() {
		return new DefaultShardManagerBuilder()
			.setToken(this.core.getConfig().token)
			.setHttpClientBuilder(new OkHttpClient.Builder()
					.protocols(Arrays.asList(Protocol.HTTP_2))) //TODO check if work
			.setSessionController(new WuffySessionController(this.gatewayBot))

			//Provider
			.setEventManagerProvider(core.getEventManagerAdapter())
			.setStatusProvider(id -> this.status.getStatusType(OnlineStatus.class))
			.setGameProvider(id -> this.status.getGameUrl() != null && Game.isValidStreamingUrl(this.status.getGameUrl()) ?
				Game.of(this.status.getGameType(GameType.class), this.status.getGameName(), this.status.getGameName()) :
				Game.of(this.status.getGameType(GameType.class), this.status.getGameName()))

			//Sharding
			.setShardsTotal(this.shardsTotal)
			.setShards(this.shardIds);
	}

	@Deprecated
	@Override
	public void addListener(EventListener listener) {
		throw new UnsupportedOperationException("This function is not supported");
	}

	@Override
	public void login() {
		try {
			try {
				if(this.shardManager != null)
					this.logout();
			} finally {
				this.shardManager = this.buildShardManagerBuilder().build();
			}
		} catch (LoginException | IllegalArgumentException e) {
			Logger.fatal("JDAAdapter", "Error by login", e);
		}
	}

	@Override
	public void logout() {
		if(this.shardManager != null)
			this.shardManager.shutdown();
		this.shardManager = null;
	}

	public void restart() {
		try {
			this.logout();
		} finally {
			this.login();
		}
	}

	public ShardManager getShardManager() {
		return this.shardManager;
	}
}