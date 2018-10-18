package net.wuffy.bot.jda.sharding;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDA.ShardInfo;

public class WuffyShardInfo extends JDA.ShardInfo {

	private int shardId;
	private int shardTotal;

	public WuffyShardInfo(int shardId, int shardTotal) {
		super(shardId, shardTotal);

		this.shardId = shardId;
		this.shardTotal = shardId;
	}

	@Override
	public int getShardId() {
		return this.shardId;
	}

	@Override
	public String getShardString() {
		return String.format("[%s/%s]", Integer.toString(this.shardId), Integer.toString(this.shardTotal));
	}

	@Override
	public int getShardTotal() {
		return this.shardTotal;
	}

	public void setShardTotal(int shardTotal) {
		this.shardTotal = shardTotal;
	}

	@Override
	public String toString() {
		return String.format("Shard %s", this.getShardString());
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ShardInfo))
			return false;

		ShardInfo shardInfo = (ShardInfo) o;

		return shardInfo.getShardId() == this.shardId && shardInfo.getShardTotal() == this.shardTotal;
	}
}