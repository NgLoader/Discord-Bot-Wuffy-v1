package net.wuffy.network.music;

import net.wuffy.network.INetHandler;
import net.wuffy.network.music.client.CPacketMusicAction;
import net.wuffy.network.music.client.CPacketMusicPlay;
import net.wuffy.network.music.client.CPacketMusicPlaylist;
import net.wuffy.network.music.client.CPacketMusicStop;
import net.wuffy.network.music.client.CPacketMusicUpdateShard;

public interface INetHandlerMusicClient extends INetHandler {

	public void handleMusicPlay(CPacketMusicPlay musicPlay);

	public void handleMusicPlaylist(CPacketMusicPlaylist musicPlaylist);

	public void handleMusicStop(CPacketMusicStop musicStop);

	public void handleMusicAction(CPacketMusicAction musicAction);

	public void handleMusicUpdateShard(CPacketMusicUpdateShard musicUpdateShard);
}