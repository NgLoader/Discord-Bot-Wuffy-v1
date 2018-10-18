package net.wuffy.bot.jda.sharding;

import java.util.EnumSet;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import javax.security.auth.login.LoginException;

import com.neovisionaries.ws.client.WebSocketFactory;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.utils.SessionController;
import net.dv8tion.jda.core.utils.cache.CacheFlag;
import okhttp3.OkHttpClient;

public class WuffyJDAImpl extends JDAImpl {

	public WuffyJDAImpl(AccountType accountType, String token, SessionController controller, OkHttpClient httpClient,
			WebSocketFactory wsFactory, ScheduledExecutorService rateLimitPool, ScheduledExecutorService gatewayPool,
			ExecutorService callbackPool, boolean autoReconnect, boolean audioEnabled, boolean useShutdownHook,
			boolean bulkDeleteSplittingEnabled, boolean retryOnTimeout, boolean enableMDC,
			boolean shutdownRateLimitPool, boolean shutdownGatewayPool, boolean shutdownCallbackPool, int poolSize,
			int maxReconnectDelay, ConcurrentMap<String, String> contextMap, EnumSet<CacheFlag> cacheFlags) {
		super(accountType, token, controller, httpClient, wsFactory, rateLimitPool, gatewayPool, callbackPool, autoReconnect,
				audioEnabled, useShutdownHook, bulkDeleteSplittingEnabled, retryOnTimeout, enableMDC, shutdownRateLimitPool,
				shutdownGatewayPool, shutdownCallbackPool, poolSize, maxReconnectDelay, contextMap, cacheFlags);
	}

	public void setTotalShards(int shards) {
		((WuffyShardInfo)this.shardInfo).setShardTotal(shards);

		this.contextMap.put("jda.shard", this.shardInfo.getShardString());
		this.contextMap.put("jda.shard.total", String.valueOf(this.shardInfo.getShardTotal()));
	}
}