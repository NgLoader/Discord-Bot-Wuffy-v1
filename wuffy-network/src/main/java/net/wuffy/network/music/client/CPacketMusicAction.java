package net.wuffy.network.music.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicClient;

public class CPacketMusicAction implements Packet<INetHandlerMusicClient> {

	private EnumMusicAction action;

	private long guildId;

	public CPacketMusicAction() { }

	public CPacketMusicAction(EnumMusicAction action, long guildId) {
		this.action = action;
		this.guildId = guildId;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.action = packetBuffer.readEnum(EnumMusicAction.class);
		this.guildId = packetBuffer.readLong();
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeEnum(this.action);
		packetBuffer.writeLong(this.guildId);
	}

	@Override
	public void handle(INetHandlerMusicClient handler) {
		handler.handleMusicAction(this);
	}

	public EnumMusicAction getAction() {
		return this.action;
	}

	public long getGuildId() {
		return this.guildId;
	}

	public enum EnumMusicAction {
		NEXT, LAST, RESTART, RESUME, PAUSE, SHUFFLE, UNSHUFFLE
	}
}