package net.wuffy.network.master.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterClient;
import net.wuffy.network.master.server.SPacketMasterStatsUpdate;

public class CPacketMasterStatsUpdate implements Packet<INetHandlerMasterClient> {

	private int shardId;

	private SPacketMasterStatsUpdate statsUpdate;

	public CPacketMasterStatsUpdate() { }

	public CPacketMasterStatsUpdate(int shardId, SPacketMasterStatsUpdate statsUpdate) {
		this.shardId = shardId;
		this.statsUpdate = statsUpdate;
	}

	public CPacketMasterStatsUpdate(int shardId, int guildCount, int channelCount, int textChannelCount, int roleCount, int userOnlineCount, int userAwayCount, int userDNDCount) {
		this.shardId = shardId;
		this.statsUpdate = new SPacketMasterStatsUpdate(guildCount, channelCount, textChannelCount, roleCount, userOnlineCount, userAwayCount, userDNDCount);
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
			this.statsUpdate = new SPacketMasterStatsUpdate();
		this.statsUpdate.read(packetBuffer);
	}

	@Override
	public void handle(INetHandlerMasterClient handler) {
		handler.handleMasterStatsUpdate(this);
	}

	public int getShardId() {
		return this.shardId;
	}

	public SPacketMasterStatsUpdate getStatsUpdate() {
		return this.statsUpdate;
	}
}