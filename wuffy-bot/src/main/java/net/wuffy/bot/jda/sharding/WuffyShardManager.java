package net.wuffy.bot.jda.sharding;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.IntFunction;

import javax.security.auth.login.LoginException;

import com.neovisionaries.ws.client.WebSocketFactory;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.bot.utils.cache.ShardCacheView;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.managers.impl.PresenceImpl;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.cache.CacheFlag;
import net.wuffy.bot.BotConfig;
import net.wuffy.network.master.client.CPacketMasterSettings;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class WuffyShardManager implements ShardManager {

	private final Queue<Integer> queue = new ConcurrentLinkedQueue<Integer>();

	private final BotConfig config;

	private WuffyShardCacheView shards;

	private OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder().protocols(Arrays.asList(Protocol.HTTP_2));
	private OkHttpClient httpClient = this.httpClientBuilder.build();

	private SessionController controller;

	private EnumSet<CacheFlag> cacheFlags = EnumSet.allOf(CacheFlag.class);

	private WebSocketFactory webSocketFactory = new WebSocketFactory();

	private List<Object> listeners;
	private List<IntFunction<Object>> listenerProviders;

	private CPacketMasterSettings gatewayBot;

	public WuffyShardManager(BotConfig config, CPacketMasterSettings gatewayBot) {
		this.config = config;

		this.controller = new WuffySessionController(this.gatewayBot = gatewayBot);
	}

	@Override
	public int getShardsQueued() {
		return 0;
	}

	@Override
	public ShardCacheView getShardCache() {
		return this.shards;
	}

	@Override
	public void restart() {
	}

	@Override
	public void restart(int id) {
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void shutdown(int shardId) {
	}

	@Override
	public void start(int shardId) {
	}

	public JDAImpl buildShard(int shardId) throws LoginException {
		final JDAImpl jda = new WuffyJDAImpl(
				AccountType.BOT, //accountType
				this.config.token, //token
				controller, //controller
				httpClient, //httpClient
				this.webSocketFactory, //wsFactory
				null, //rateLimitPool
				null, //gatewayPool
				null, //callbackPool
				true, //autoReconnect
				false, //audioEnable
				true, //useShutdownHook
				false, //bulkDeleteSplittingEnabled
				true, //retryOnTimeout
				false, //enableMDC
				true, //shutdownRateLimitPool
				true, //shutdownGatewayPool
				true, //shutdownCallbackPool
				5, //poolSize
				60, //maxReconnectDelay
				null, //contextMap
				this.cacheFlags //cacheFlags
			);

		jda.asBot().setShardManager(this);

		this.listeners.forEach(jda::addEventListener);
		this.listenerProviders.forEach(provider -> jda.addEventListener(provider.apply(shardId)));

		jda.setStatus(JDA.Status.INITIALIZED);

		PresenceImpl presence = (PresenceImpl) jda.getPresence();
		presence.setCacheGame(this.config.game.url != null && !this.config.game.url.isEmpty() ? 
				Game.of(this.config.game.gameType, this.config.game.name, this.config.game.url) :
				Game.of(this.config.game.gameType, this.config.game.name));
		presence.setCacheStatus(this.config.status);

		jda.login(this.gatewayBot.getUrl(), new WuffyShardInfo(shardId, gatewayBot.getShards()), true, false);

		return jda;
	}
}