package net.wuffy.network.master.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.master.INetHandlerMasterServer;

public class SPacketMasterInit implements Packet<INetHandlerMasterServer> {

	private long startupTime;

	public SPacketMasterInit() { }

	public SPacketMasterInit(long startupTime) {
		this.startupTime = startupTime;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.startupTime = packetBuffer.readLong();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeLong(this.startupTime);
	}

	@Override
	public void handle(INetHandlerMasterServer handler) {
		handler.handleMasterInit(this);
	}

	public long getStartupTime() {
		return this.startupTime;
	}
}