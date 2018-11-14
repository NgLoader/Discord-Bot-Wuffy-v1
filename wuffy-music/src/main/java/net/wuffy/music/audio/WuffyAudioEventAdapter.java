package net.wuffy.music.audio;

import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;

import net.wuffy.music.audio.module.ModuleEventManager;

public class WuffyAudioEventAdapter extends AudioEventAdapter {

	private ModuleEventManager eventManager;

	public WuffyAudioEventAdapter(ModuleEventManager eventManager) {
		this.eventManager = eventManager;
	}

	@Override
	public void onEvent(AudioEvent event) {
		this.eventManager.onEvent(event);
	}
}