package net.wuffy.network.music.client;

import java.io.IOException;

import net.wuffy.network.Packet;
import net.wuffy.network.PacketBuffer;
import net.wuffy.network.music.INetHandlerMusicClient;

public class CPacketMusicAction implements Packet<INetHandlerMusicClient> {

	private EnumMusicAction action;

	private long guildId;

	private int valueInt;

	public CPacketMusicAction() { }

	public CPacketMusicAction(EnumMusicAction action, long guildId) {
		this.action = action;
		this.guildId = guildId;
	}

	public CPacketMusicAction(EnumMusicAction action, long guildId, int valueInt) {
		this.action =  action;
		this.guildId = guildId;
		this.valueInt = valueInt;
	}

	@Override
	public void read(PacketBuffer packetBuffer) throws IOException {
		this.action = packetBuffer.readEnum(EnumMusicAction.class);
		this.guildId = packetBuffer.readLong();

		switch (this.action) {
		case JUMP:
		case SKIP:
		case VOLUME:
			this.valueInt = packetBuffer.readInt();
			break;

		default:
			break;
		}
	}

	@Override
	public void write(PacketBuffer packetBuffer) throws IOException {
		packetBuffer.writeEnum(this.action);
		packetBuffer.writeLong(this.guildId);

		switch (this.action) {
		case JUMP:
		case SKIP:
		case VOLUME:
			packetBuffer.writeInt(this.valueInt);
			break;

		default:
			break;
		}
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

	public int getValueInt() {
		return this.valueInt;
	}

	public enum EnumMusicAction {
		NEXT, LAST, RESTART, RESUME, PAUSE, SHUFFLE, REPEAT, SKIP, JUMP, VOLUME
	}
}