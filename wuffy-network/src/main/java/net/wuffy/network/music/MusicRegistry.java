package net.wuffy.network.music;

import net.wuffy.network.music.client.CPacketMusicPlay;
import net.wuffy.network.music.client.CPacketMusicPlaylist;
import net.wuffy.network.music.client.CPacketMusicStop;
import net.wuffy.network.music.client.CPacketMusicAction;
import net.wuffy.network.music.client.CPacketMusicUpdateShard;
import net.wuffy.network.music.server.SPacketMusicEvent;
import net.wuffy.network.universal.UniversalRegistry;

public final class MusicRegistry extends UniversalRegistry {

	public static final MusicRegistry INSTANCE = new MusicRegistry();

	protected MusicRegistry() {
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketMusicPlay.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketMusicPlaylist.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketMusicStop.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketMusicAction.class);
		this.addPacket(EnumProtocolDirection.CLIENTBOUND, CPacketMusicUpdateShard.class);

		this.addPacket(EnumProtocolDirection.SERVERBOUND, SPacketMusicEvent.class);
	}
}