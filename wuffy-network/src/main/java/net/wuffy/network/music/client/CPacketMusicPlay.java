package net.wuffy.network.music.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicClient;

public class CPacketMusicPlay implements Packet<INetHandlerMusicClient> {

	private long guildId;

	private String url;

	public CPacketMusicPlay() { }

	public CPacketMusicPlay(int guildId, String url) {
		this.guildId = guildId;
		this.url = url;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.guildId = packetBuffer.readInt();
		this.url = packetBuffer.readString();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeLong(this.guildId);
		packetBuffer.writeString(this.url);
	}

	@Override
	public void handle(INetHandlerMusicClient handler) {
		handler.handleMusicPlay(this);
	}

	public long getGuildId() {
		return this.guildId;
	}

	public String getUrl() {
		return this.url;
	}
}