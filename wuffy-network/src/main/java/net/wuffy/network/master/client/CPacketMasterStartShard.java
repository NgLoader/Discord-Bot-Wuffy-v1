package net.wuffy.network.master.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterClient;

public class CPacketMasterStartShard implements Packet<INetHandlerMasterClient> {

	private int shardId;

	public CPacketMasterStartShard() { }

	public CPacketMasterStartShard(int shardId) {
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
		handler.handleMasterStartShard(this);
	}

	public int getShardId() {
		return this.shardId;
	}
}