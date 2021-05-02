package net.wuffy.music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.DiscordHelper;
import net.wuffy.common.util.ITickable;
import net.wuffy.music.audio.WuffyAudioGuild;
import net.wuffy.music.audio.WuffyAudioHandler;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class JDAHandler implements ITickable {

	private static final long TIMEOUT_DELAY = 30000;

	private final String token;
	private int shardCount;

	private Map<Integer, ShardManager> shardManagers = new HashMap<Integer, ShardManager>();

	private Map<Long, Integer> guildsByShardCount = new HashMap<Long, Integer>();

	private Map<Long, Long> guildTimeouts = new HashMap<Long, Long>();

	public JDAHandler(String token, int shardCount) {
		this.token = token;
		this.shardCount = shardCount;
	}

	private ShardManager buildShardManager(String token, int shardCount) throws LoginException, IllegalArgumentException {
		return new DefaultShardManagerBuilder()
				.setToken(token)
				.setShardsTotal(shardCount)
				.setShards(new ArrayList<Integer>())
				.setAudioEnabled(true)
				.setAutoReconnect(true)
				.setUseShutdownNow(true)
				.setBulkDeleteSplittingEnabled(false)
				.setEventManagerProvider(new JDAEventManager())
				.setDisabledCacheFlags(EnumSet.of(CacheFlag.EMOTE, CacheFlag.GAME))
				.setHttpClientBuilder(new OkHttpClient.Builder()
						.protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1)))
				.build();
	}

	@Override
	public void update() {
		try {
			for(Map.Entry<Integer, ShardManager> entry : this.shardManagers.entrySet()) {
				for(JDA jda : entry.getValue().getShards()) {
					for(AudioManager audioManager : jda.getAudioManagers()) {
						long guildId = audioManager.getGuild().getIdLong();
						WuffyAudioGuild audioGuild = WuffyAudioHandler.getGuild(guildId);

						if(!audioManager.isAttemptingToConnect() && (
								audioGuild == null										||
								audioGuild.getAudioPlayer().getPlayingTrack() == null 	||
								audioManager.getSendingHandler() == null 				||
//								audioManager.getSendingHandler().canProvide() 			|| //Unknown error (already checking is null ^)
								audioManager.getConnectedChannel() == null 				||
								audioManager.getConnectedChannel().getMembers().stream().filter(member -> !member.getUser().isBot()).count() == 0)) { //Check is only the bot in this channel

							if(!this.guildTimeouts.containsKey(guildId))
								this.guildTimeouts.put(guildId, System.currentTimeMillis() + JDAHandler.TIMEOUT_DELAY);
							else if(this.guildTimeouts.get(guildId) < System.currentTimeMillis()) {
								this.guildTimeouts.remove(guildId);

								audioManager.setAutoReconnect(false);
								audioManager.setSendingHandler(null);
								audioManager.closeAudioConnection();

								WuffyAudioHandler.removeGuild(audioManager.getGuild().getIdLong());
								//TODO send packet to bot (timeout)

								Logger.debug("JDAHandler", String.format("Disconnecting from voice guild \"%s\" while idle", Long.toString(guildId)));
							}
						} else if(this.guildTimeouts.containsKey(guildId))
							this.guildTimeouts.remove(guildId);
					}

					if(jda.getStatus() == Status.CONNECTED && !jda.getAudioManagerCache().stream().anyMatch(audioManager -> audioManager.isConnected() || audioManager.isAttemptingToConnect())) {
						Logger.debug("JDAHandler", String.format("Stopping shard \"%s\" while no audio streams needed.", jda.getShardInfo().getShardString()));

						jda.shutdownNow();
					}
				}

				if(entry.getValue().getShardsRunning() == 0 && entry.getKey() != this.shardCount) {
					Logger.debug("JDAHandler", String.format("Stopping ShardManager while no shard longer exist and not the correct shardManager (shard count changed)"));

					this.shardManagers.remove(entry.getKey());
					entry.getValue().shutdown();
				}
			}
		} catch(Exception e) {
			Logger.fatal("JDAHandler", "Error by updateing", e);
		}
	}

	public synchronized ShardManager getShardManager() {
		return this.getShardManager(this.shardCount);
	}

	public synchronized ShardManager getShardManager(int shardCount) {
		if(!this.shardManagers.containsKey(this.shardCount))
			try {
				Logger.debug("JDAHandler", String.format("Creating ShardManager for shard count \"%s\"", Integer.toString(this.shardCount)));
				this.shardManagers.put(this.shardCount, this.buildShardManager(this.token, this.shardCount));
			} catch (LoginException | IllegalArgumentException e) {
				Logger.fatal("JDAHandler", "Error by starting shardManager", e);
			}

		return this.shardManagers.get(this.shardCount);
	}

	public Guild getGuild(long guildId) {
		if(!this.guildsByShardCount.containsKey(guildId)) {
			synchronized (this.guildsByShardCount) {
				int shardCount = this.shardCount;
				int shardId = DiscordHelper.getShardId(guildId, shardCount);
				ShardManager shardManager = this.getShardManager(shardCount);

				if(shardManager.getShardById(shardId) == null)
					shardManager.start(shardId);

				this.guildsByShardCount.put(guildId, shardCount);

				while(shardManager.getStatus(shardId) != Status.CONNECTED) //Waiting
					continue;

				return shardManager.getShardById(shardId).getGuildById(guildId);
			}
		}

		return this.getShardManager(this.guildsByShardCount.get(guildId)).getGuildById(guildId);
	}

	public void changeShardCount(int newShardCount) {
		this.shardCount = newShardCount;
	}
}