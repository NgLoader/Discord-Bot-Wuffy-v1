package net.wuffy.network.master.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterClient;

public class CPacketMasterShardUpdate implements Packet<INetHandlerMasterClient> {

	private EnumMasterShard type;

	private int value;

	public CPacketMasterShardUpdate() { }

	public CPacketMasterShardUpdate(EnumMasterShard type, int value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.type = packetBuffer.readEnum(EnumMasterShard.class);
		this.value = packetBuffer.readInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeEnum(this.type);
		packetBuffer.writeInt(this.value);
	}

	@Override
	public void handle(INetHandlerMasterClient handler) {
		handler.handleMasterShardUpdate(this);
	}

	public EnumMasterShard getType() {
		return this.type;
	}

	public int getValue() {
		return this.value;
	}

	public enum EnumMasterShard {
		START, STOP, SHARDCOUNT
	}
}