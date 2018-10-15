package net.wuffy.network.master.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterServer;

public class SPacketMasterStoppedShard implements Packet<INetHandlerMasterServer> {

	private int shardId;

	public SPacketMasterStoppedShard() { }

	public SPacketMasterStoppedShard(int shardId) {
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
	public void handle(INetHandlerMasterServer handler) {
		handler.handleMasterStoppedShard(this);
	}

	public int getShardId() {
		return this.shardId;
	}
}