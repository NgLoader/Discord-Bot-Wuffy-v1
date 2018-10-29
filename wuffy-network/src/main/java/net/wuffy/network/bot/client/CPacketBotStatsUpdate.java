package net.wuffy.network.bot.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.bot.INetHandlerBotClient;
import net.wuffy.network.bot.server.SPacketBotStatsUpdate;

public class CPacketBotStatsUpdate implements Packet<INetHandlerBotClient> {

	private int shardId;

	private SPacketBotStatsUpdate statsUpdate;

	public CPacketBotStatsUpdate() { }

	public CPacketBotStatsUpdate(int shardId, SPacketBotStatsUpdate statsUpdate) {
		this.shardId = shardId;
		this.statsUpdate = statsUpdate;
	}

	public CPacketBotStatsUpdate(int shardId, int guildCount, int channelCount, int textChannelCount, int roleCount, int userOnlineCount, int userAwayCount, int userDNDCount) {
		this.shardId = shardId;
		this.statsUpdate = new SPacketBotStatsUpdate(guildCount, channelCount, textChannelCount, roleCount, userOnlineCount, userAwayCount, userDNDCount);
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeInt(this.shardId);
		this.statsUpdate.read(packetBuffer);
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		this.shardId = packetBuffer.readInt();
		
		if(this.statsUpdate == null) //Only create a new class when no exist otherwise the old class will be overriten by the read function
			this.statsUpdate = new SPacketBotStatsUpdate();
		this.statsUpdate.read(packetBuffer);
	}

	@Override
	public void handle(INetHandlerBotClient handler) {
		handler.handleBotStatsUpdate(this);
	}

	public int getShardId() {
		return this.shardId;
	}

	public SPacketBotStatsUpdate getStatsUpdate() {
		return this.statsUpdate;
	}
}