package net.wuffy.network.music.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicClient;

public class CPacketMusicStop implements Packet<INetHandlerMusicClient> {

	private long guildId;

	public CPacketMusicStop() { }

	public CPacketMusicStop(long guildId) {
		this.guildId = guildId;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.guildId = packetBuffer.readLong();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeLong(this.guildId);
	}

	@Override
	public void handle(INetHandlerMusicClient handler) {
		handler.handleMusicStop(this);
	}

	public long getGuildId() {
		return this.guildId;
	}
}