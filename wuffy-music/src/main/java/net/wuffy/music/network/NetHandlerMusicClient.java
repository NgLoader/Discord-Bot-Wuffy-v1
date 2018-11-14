package net.wuffy.music.network;

import net.wuffy.common.logger.Logger;
import net.wuffy.music.Music;
import net.wuffy.music.audio.WuffyAudioGuild;
import net.wuffy.music.audio.WuffyAudioHandler;
import net.wuffy.music.audio.WuffyAudioQueue;
import net.wuffy.network.NetworkManager;
import net.wuffy.network.music.INetHandlerMusicClient;
import net.wuffy.network.music.client.CPacketMusicAction;
import net.wuffy.network.music.client.CPacketMusicPlay;
import net.wuffy.network.music.client.CPacketMusicPlaylist;
import net.wuffy.network.music.client.CPacketMusicStop;
import net.wuffy.network.music.client.CPacketMusicUpdateShard;
import net.wuffy.network.universal.INetHandlerUniversalClient;
import net.wuffy.network.universal.client.CPacketUniversalDisconnect;
import net.wuffy.network.universal.client.CPacketUniversalKeepAlive;
import net.wuffy.network.universal.server.SPacketUniversalKeepAlive;

public class NetHandlerMusicClient implements INetHandlerUniversalClient, INetHandlerMusicClient {

	private NetworkManager networkManager;

	public NetHandlerMusicClient(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	/*
	 * TODO - Add Volume
	 */

	@Override
	public void handleMusicPlay(CPacketMusicPlay musicPlay) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleMusicPlaylist(CPacketMusicPlaylist musicPlaylist) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleMusicStop(CPacketMusicStop musicStop) {
		WuffyAudioHandler.removeGuild(musicStop.getGuildId());
	}

	@Override
	public void handleMusicAction(CPacketMusicAction musicAction) {
		WuffyAudioGuild audioGuild = WuffyAudioHandler.getGuild(musicAction.getGuildId());
		WuffyAudioQueue audioQueue = audioGuild.getAudioQueue();

		if(audioGuild != null) {
			switch(musicAction.getAction()) {
				case LAST:
					audioQueue.play(audioQueue.getLast());
					break;

				case NEXT:
					audioQueue.play(audioQueue.getNext(true));
					break;

				case PAUSE:
					audioGuild.getAudioPlayer().setPaused(true);
					break;

				case RESTART:
					audioQueue.reset();
					audioQueue.play(audioQueue.getNext(false));
					break;

				case RESUME:
					audioGuild.getAudioPlayer().setPaused(false);
					break;

				case SHUFFLE:
					audioQueue.setShuffled(true);
					break;

				case UNSHUFFLE:
					audioQueue.setShuffled(false);
					break;

				default:
					break;
			}
		}
	}

	@Override
	public void handleMusicUpdateShard(CPacketMusicUpdateShard musicUpdateShard) {
		Music.getInstance().getJdaHandler().changeShardCount(musicUpdateShard.getShardCount());
	}

	@Override
	public void handleKeepAlive(CPacketUniversalKeepAlive keepAlive) {
		this.networkManager.sendPacket(new SPacketUniversalKeepAlive(keepAlive.getTimestamp()));
	}

	@Override
	public void handleDisconnect(CPacketUniversalDisconnect disconnect) {
		this.onDisconnect(disconnect.getReason());
	}

	@Override
	public void onDisconnect(String reason) {
		Logger.info(String.format("Disconnected: %s", reason));
	}
}