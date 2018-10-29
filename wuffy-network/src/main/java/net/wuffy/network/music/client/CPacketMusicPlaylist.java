package net.wuffy.network.music.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicClient;

public class CPacketMusicPlaylist implements Packet<INetHandlerMusicClient> {

	private long guildId;

	private int playlistId;

	public CPacketMusicPlaylist() { }

	public CPacketMusicPlaylist(long guildId, int playlistId) {
		this.guildId = guildId;
		this.playlistId = playlistId;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.guildId = packetBuffer.readLong();
		this.playlistId = packetBuffer.readInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeLong(this.guildId);
		packetBuffer.writeInt(this.playlistId);
	}

	@Override
	public void handle(INetHandlerMusicClient handler) {
		handler.handleMusicPlaylist(this);
	}

	public long getGuildId() {
		return this.guildId;
	}

	public int getPlaylistId() {
		return this.playlistId;
	}
}