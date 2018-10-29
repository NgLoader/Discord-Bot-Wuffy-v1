package net.wuffy.network.music.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicClient;

public class CPacketMusicUpdateShard implements Packet<INetHandlerMusicClient> {

	private int shardCount;

	public CPacketMusicUpdateShard() { }

	public CPacketMusicUpdateShard(int shardCount) {
		this.shardCount = shardCount;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.shardCount = packetBuffer.readInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeInt(this.shardCount);
	}

	@Override
	public void handle(INetHandlerMusicClient handler) {
		handler.handleMusicUpdateShard(this);
	}

	public int getShardCount() {
		return this.shardCount;
	}
}