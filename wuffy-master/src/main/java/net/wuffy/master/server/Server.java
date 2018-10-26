package net.wuffy.master.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.wuffy.common.Defaults;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.IWuffyPhantomReference;
import net.wuffy.master.sharding.Shard;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.master.client.CPacketMasterShardUpdate;
import net.wuffy.network.master.client.CPacketMasterShardUpdate.EnumMasterShard;
import net.wuffy.network.master.server.SPacketMasterHallo;
import net.wuffy.network.master.server.SPacketMasterStatsUpdate;
import net.wuffy.network.master.server.SPacketMasterSystemUpdate;

public class Server implements IWuffyPhantomReference {

	private final NetworkManager networkManager;

	private final UUID uuid;

	private String name;

	private SPacketMasterHallo defaultStats;
	private SPacketMasterSystemUpdate systemUpdate;
	private SPacketMasterStatsUpdate statsUpdate;

	private Map<Integer, Shard> shardsRunning = new HashMap<Integer, Shard>();

	public Server(NetworkManager networkManager, UUID uuid, String name) {
		this.networkManager = networkManager;
		this.uuid = uuid;
		this.name = name;

		WuffyPhantomRefernce.getInstance().add(this, String.format("%s (%s)", this.uuid, this.name));
	}

	public void destroy() {
		shardsRunning.values().forEach(shard -> this.stopShard(shard.getId()));

		if(this.networkManager != null)
			this.networkManager.close("Destoryed");

		this.shardsRunning = null;
		this.name = null;
		this.defaultStats = null;
		this.systemUpdate = null;
		this.statsUpdate = null;
	}

	public void updateShardCount(int newShardCount) {
		this.networkManager.sendPacket(new CPacketMasterShardUpdate(EnumMasterShard.SHARDCOUNT, newShardCount));
	}

	public void startShard(int shardId) {
		try {
			if(shardsRunning.containsKey(shardId)) {
				Logger.warn("ShardHandler", String.format("Shard \"%s\" already started!", shardId));
				return;
			}

			Shard shard = new Shard(this, shardId);
			shardsRunning.put(shardId, shard);

			this.networkManager.sendPacket(new CPacketMasterShardUpdate(EnumMasterShard.START, shardId));
			Logger.debug("ShardHandler", String.format("Shard \"%s\" successful starting on \"%s\".", shardId, this.name));
		} catch(Exception e) {
			Logger.fatal("ShardHandler", String.format("Failed to start shard \"%s\"", shardId), e);
		}
	}

	public void stopShard(int shardId) {
		try {
			if(!shardsRunning.containsKey(shardId)) {
				Logger.warn("ShardHandler", String.format("Shard \"%s\" not running!", shardId));
				return;
			}

			Shard shard = shardsRunning.remove(shardId);

			if(shard == null) {
				Logger.warn("ShardHandler", String.format("Shard \"%s\" was null by removing!", shardId));
				return;
			}

			this.networkManager.sendPacket(new CPacketMasterShardUpdate(EnumMasterShard.STOP, shardId));
		} catch(Exception e) {
			Logger.fatal("ShardHandler", String.format("Failed to stop shard \"%s\"", shardId), e);
		}
	}

	public boolean canShardStart() {
		if(!this.isReady())
			return false;

		if(this.defaultStats.getTotalMemory() - Defaults.SHARD_RAM_COST < this.getUsedMemoryByShardCount())
			return false;

		if(this.statsUpdate != null &&
				this.systemUpdate.getCpuUsage() < 80 && //Check if the currently CPU Usage under 80%
				this.systemUpdate.getFreeMemory() - Defaults.SHARD_RAM_COST > 512) //Check has the currently freeMemory with the SHARD_RAM_COST more then 520MB free
			return true;
		return false;
	}

	public void handlePacketHallo(SPacketMasterHallo packetMasterHallo) {
		this.defaultStats = packetMasterHallo;
	}

	public void handlePacketSystemUpdate(SPacketMasterSystemUpdate packetSystemUpdate) {
		this.systemUpdate = packetSystemUpdate;
	}

	public void handlePacketStatsUpdate(SPacketMasterStatsUpdate packetStatsUpdate) {
		this.statsUpdate = packetStatsUpdate;
	}

	public boolean isReady() {
		return this.statsUpdate != null && this.systemUpdate != null && this.defaultStats != null;
	}

	public int getUsedMemoryByShardCount() {
		return this.shardsRunning.size() * Defaults.SHARD_RAM_COST;
	}

	public boolean isShardRunning(int shardId) {
		return this.shardsRunning.containsKey(shardId);
	}

	public List<Integer> getShardIdsRunning() {
		return Collections.unmodifiableList(new ArrayList<>(this.shardsRunning.keySet()));
	}

	public Map<Integer, Shard> getShardsRunning() {
		return Collections.unmodifiableMap(this.shardsRunning);
	}

	public NetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

	public UUID getId() {
		return this.uuid;
	}

	public SPacketMasterStatsUpdate getStatsUpdate() {
		return this.statsUpdate;
	}

	public SPacketMasterHallo getStats() {
		return this.defaultStats;
	}

	public SPacketMasterSystemUpdate getSystemUpdate() {
		return this.systemUpdate;
	}
}