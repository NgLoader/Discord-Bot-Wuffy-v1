package net.wuffy.network.music.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicClient;

public class CPacketMusicPlay implements Packet<INetHandlerMusicClient> {

	private int id;

	private long guildId;

	public CPacketMusicPlay() { }

	public CPacketMusicPlay(int id, int guildId) {
		this.id = id;
		this.guildId = guildId;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.id = packetBuffer.readInt();
		this.guildId = packetBuffer.readInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeInt(this.id);
		packetBuffer.writeLong(this.guildId);
	}

	@Override
	public void handle(INetHandlerMusicClient handler) {
		handler.handleMusicPlay(this);
	}

	public int getId() {
		return this.id;
	}

	public long getGuildId() {
		return this.guildId;
	}
}