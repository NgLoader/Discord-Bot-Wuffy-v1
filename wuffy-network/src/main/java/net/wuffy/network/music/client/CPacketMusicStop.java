package net.wuffy.network.music.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicClient;

public class CPacketMusicStop implements Packet<INetHandlerMusicClient> {

	private int id;

	public CPacketMusicStop() { }

	public CPacketMusicStop(int id) {
		this.id = id;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.id = packetBuffer.readInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeInt(this.id);
	}

	@Override
	public void handle(INetHandlerMusicClient handler) {
		handler.handleMusicStop(this);
	}

	public int getId() {
		return this.id;
	}
}