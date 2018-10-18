package net.wuffy.bot.jda.sharding;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.neovisionaries.ws.client.WebSocketFactory;

import net.dv8tion.jda.bot.sharding.DefaultShardManager;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.bot.utils.cache.ShardCacheView;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.cache.CacheFlag;
import net.wuffy.bot.BotConfig;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class WuffyShardManager implements ShardManager {

	private final Queue<Integer> queue = new ConcurrentLinkedQueue<Integer>();

	private final BotConfig config;

	private WuffyShardCacheView shards;

	private OkHttpClient httpClient;
	private OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder().protocols(Arrays.asList(Protocol.HTTP_2));

	private SessionController controller = new WuffySessionController();

	private EnumSet<CacheFlag> cacheFlags = EnumSet.allOf(CacheFlag.class);

	private WebSocketFactory webSocketFactory = new WebSocketFactory();

	public WuffyShardManager(BotConfig config) {
		this.config = config;
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

	public JDAImpl buildShard(int shardId) {
		if(this.httpClient == null)
			this.httpClient = this.httpClientBuilder.build();

		final JDAImpl jda = new JDAImpl(
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

		jda.setStatus(JDA.Status.INITIALIZED);

		return jda;
	}
}