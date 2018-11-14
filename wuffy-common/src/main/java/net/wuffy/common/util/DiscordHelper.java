package net.wuffy.common.util;

public class DiscordHelper {

	public static int getShardId(long guildId, int shardCount) {
		return Long.valueOf((guildId >> 22) % shardCount).intValue();
	}
}