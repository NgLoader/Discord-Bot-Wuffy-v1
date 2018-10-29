package net.wuffy.network.music;

import net.wuffy.network.INetHandler;
import net.wuffy.network.music.server.SPacketMusicEvent;

public interface INetHandlerMusicServer extends INetHandler {

	public void handleMusicEvent(SPacketMusicEvent musicEvent);
}