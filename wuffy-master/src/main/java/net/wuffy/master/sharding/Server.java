package net.wuffy.master.sharding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.util.IWuffyPhantomReference;

public class Server implements IWuffyPhantomReference {

	private final UUID uuid;

	private String name;
	private String address;

	private Map<Integer, Shard> shardsRunning = new HashMap<Integer, Shard>();

	private double cpuUsage;
	private double cpuAverage;
	private long freeMemory;
	private long freeSwap;
	private long freeDiskSpace;
	private long maxMemory; //Max memory for using
	private long shardMemoryCost; //Memory for one shard

	public Server(UUID uuid, String name, String address,
			double cpuUsage, double cpuAverage, long freeMemory, long freeSwap, long freeDiskSpace,
			long maxMemory, long shardMemoryCost) {
		this.uuid = uuid;
		this.name = name;
		this.address = address;
		this.cpuUsage = cpuUsage;
		this.cpuAverage = cpuAverage;
		this.freeMemory = freeMemory;
		this.freeSwap = freeSwap;
		this.freeDiskSpace = freeDiskSpace;
		this.maxMemory = maxMemory;
		this.shardMemoryCost = shardMemoryCost;

		WuffyPhantomRefernce.getInstance().add(this, String.format("%s (%s)", this.uuid, this.name));
	}

	public void destroy() {
		shardsRunning.values().forEach(shard -> this.stopShard(shard.getId()));

		this.shardsRunning = null;
		this.name = null;
		this.address = null;
	}

	public void startShard(int shardId) {
		try {
			if(shardsRunning.containsKey(shardId)) {
				Logger.warn("ShardHandler", String.format("Shard \"%s\" already started!", shardId));
				return;
			}

			Shard shard = new Shard(this, shardId);
			shardsRunning.put(shardId, shard);

			//TODO send start to server
			Logger.debug("ShardHandler", String.format("Shard \"%s\" successful starting on \"%s\".", shardId, this.name));
		} catch(Exception ex) {
			Logger.fatal("ShardHandler", String.format("Failed to start shard \"%s\"", shardId), ex);
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

			//TODO send stop to server
		} catch(Exception ex) {
			Logger.fatal("ShardHandler", String.format("Failed to stop shard \"%s\"", shardId), ex);
		}
	}

	public boolean canShardStart() {
		if(this.maxMemory - this.shardMemoryCost < this.getUsedMemoryByShardCount())
			return false;

		//TODO check CPU usage and more...
		return true;
	}

	public boolean isShardRunning(int shardId) {
		return this.shardsRunning.containsKey(shardId);
	}

	public List<Integer> getShardIdsRunning(){
		return Collections.unmodifiableList(new ArrayList<>(this.shardsRunning.keySet()));
	}

	public Map<Integer, Shard> getShardsRunning() {
		return Collections.unmodifiableMap(this.shardsRunning);
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public double getCPUUsage() {
		return this.cpuUsage;
	}

	public double getCPUAverage() {
		return this.cpuAverage;
	}

	public long getFreeMemory() {
		return this.freeMemory;
	}

	public long getFreeSwap() {
		return this.freeSwap;
	}

	public long getFreeDiskSpace() {
		return this.freeDiskSpace;
	}

	public long getMaxMemory() {
		return this.maxMemory;
	}

	public long getUsedMemoryByShardCount() {
		return this.shardsRunning.size() * this.shardMemoryCost;
	}

	public long getShardMemoryCost() {
		return this.shardMemoryCost;
	}
}