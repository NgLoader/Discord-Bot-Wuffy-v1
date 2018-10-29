package net.wuffy.network.bot.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.bot.INetHandlerBotServer;

public class SPacketBotStoppedShard implements Packet<INetHandlerBotServer> {

	private int shardId;

	public SPacketBotStoppedShard() { }

	public SPacketBotStoppedShard(int shardId) {
		this.shardId = shardId;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.shardId = packetBuffer.readInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeInt(this.shardId);
	}

	@Override
	public void handle(INetHandlerBotServer handler) {
		handler.handleBotStoppedShard(this);
	}

	public int getShardId() {
		return this.shardId;
	}
}