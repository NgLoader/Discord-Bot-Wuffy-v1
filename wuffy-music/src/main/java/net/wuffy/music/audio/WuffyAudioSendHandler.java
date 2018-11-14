package net.wuffy.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import net.dv8tion.jda.core.audio.AudioSendHandler;

public class WuffyAudioSendHandler implements AudioSendHandler {

	private final AudioPlayer audioPlayer;

	private AudioFrame lastFrame;

	public WuffyAudioSendHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
	}

	@Override
	public boolean canProvide() {
		if(this.lastFrame == null)
			this.lastFrame = this.audioPlayer.provide();

		return this.lastFrame != null;
	}

	@Override
	public byte[] provide20MsAudio() {
		if(this.lastFrame ==  null)
			this.lastFrame = audioPlayer.provide();

		byte[] data = this.lastFrame != null ? lastFrame.getData() : null;
		this.lastFrame = null;

		return data;
	}

	@Override
	public boolean isOpus() {
		return true;
	}
}