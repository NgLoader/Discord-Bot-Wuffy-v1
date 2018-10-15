package net.wuffy.network.master.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterClient;

public class CPacketMasterStopShard implements Packet<INetHandlerMasterClient> {

	private int shardId;

	public CPacketMasterStopShard() { }

	public CPacketMasterStopShard(int shardId) {
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
	public void handle(INetHandlerMasterClient handler) {
		handler.handleMasterStopShard(this);
	}

	public int getShardId() {
		return this.shardId;
	}
}