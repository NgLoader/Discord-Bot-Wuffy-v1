package net.wuffy.network.master.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterClient;

public class CPacketMasterStatsUpdate implements Packet<INetHandlerMasterClient> {

	private int shardId;

	private int guildCount;

	private int channelCount;
	private int textChannelCount;

	private int roleCount;

	private int userOnlineCount;
	private int userAwayCount;
	private int userDNDCount;
	private int userTotalCount; //!!!Don't send total count!!! (userOnlineCount + userAwayCount + userDNDCount)

	public CPacketMasterStatsUpdate() { }

	public CPacketMasterStatsUpdate(int shardId, int guildCount, int channelCount, int textChannelCount, int roleCount, int userOnlineCount, int userAwayCount, int userDNDCount) {
		this.shardId = shardId;
		this.guildCount = guildCount;
		this.channelCount = channelCount;
		this.textChannelCount = textChannelCount;
		this.roleCount = roleCount;
		this.userOnlineCount = userOnlineCount;
		this.userAwayCount = userAwayCount;
		this.userDNDCount = userDNDCount;

		this.userTotalCount = this.userOnlineCount + this.userAwayCount + this.userDNDCount;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeInt(this.shardId);
		packetBuffer.writeInt(this.guildCount);
		packetBuffer.writeInt(this.channelCount);
		packetBuffer.writeInt(this.textChannelCount);
		packetBuffer.writeInt(this.roleCount);
		packetBuffer.writeInt(this.userOnlineCount);
		packetBuffer.writeInt(this.userAwayCount);
		packetBuffer.writeInt(this.userDNDCount);
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		this.shardId = packetBuffer.readInt();
		this.guildCount = packetBuffer.readInt();
		this.channelCount = packetBuffer.readInt();
		this.textChannelCount = packetBuffer.readInt();
		this.roleCount = packetBuffer.readInt();
		this.userOnlineCount = packetBuffer.readInt();
		this.userAwayCount = packetBuffer.readInt();
		this.userDNDCount = packetBuffer.readInt();
	}

	@Override
	public void handle(INetHandlerMasterClient handler) {
		handler.handleMasterStatsUpdate(this);
	}

	public int getShardId() {
		return this.shardId;
	}

	public int getGuildCount() {
		return this.guildCount;
	}

	public int getUserOnlineCount() {
		return this.userOnlineCount;
	}

	public int getUserAwayCount() {
		return this.userAwayCount;
	}

	public int getUserDNDCount() {
		return this.userDNDCount;
	}

	public int getUserTotalCount() {
		return this.userTotalCount;
	}

	public int getRoleCount() {
		return this.roleCount;
	}

	public int getChannelCount() {
		return this.channelCount;
	}

	public int getTextChannelCount() {
		return this.textChannelCount;
	}
}