package net.wuffy.network.music.server;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicServer;

public class SPacketMusicEvent implements Packet<INetHandlerMusicServer> {

	private EnumMusicEvent type;

	private int songId;

	public SPacketMusicEvent() { }

	public SPacketMusicEvent(EnumMusicEvent type) {
		this.type = type;
	}

	public SPacketMusicEvent(EnumMusicEvent type, int songId) {
		this.type = type;

		if(type == EnumMusicEvent.CHANGED)
			this.songId = songId;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.type = packetBuffer.readEnum(EnumMusicEvent.class);

		if(type == EnumMusicEvent.CHANGED)
			this.songId = packetBuffer.readInt();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeEnum(this.type);
		packetBuffer.writeInt(this.songId);
	}

	@Override
	public void handle(INetHandlerMusicServer handler) {
		handler.handleMusicEvent(this);
	}

	public EnumMusicEvent getType() {
		return this.type;
	}

	public int getSongId() {
		return this.songId;
	}

	public enum EnumMusicEvent {
		STOPPED, STARTED, CHANGED
	}
}