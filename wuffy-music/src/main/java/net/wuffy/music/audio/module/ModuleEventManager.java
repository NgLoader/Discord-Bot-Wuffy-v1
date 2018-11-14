package net.wuffy.music.audio.module;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;

import net.dv8tion.jda.core.events.Event;
import net.wuffy.common.logger.Logger;
import net.wuffy.music.audio.WuffyAudioGuild;

public class ModuleEventManager {

	private WuffyAudioGuild audioGuild;

	private List<ModuleEventAdapter> listeners = new ArrayList<ModuleEventAdapter>();

	public ModuleEventManager(WuffyAudioGuild audioGuild) {
		this.audioGuild = audioGuild;
	}

	public void onEvent(ModuleEvent event) {
		for(ModuleEventAdapter listener : this.listeners)
			try {
				listener.onEvent(event);
			} catch(Exception e) {
				Logger.fatal("EventManager", "Error executing module event", e);
			}
	}

	public void onEvent(AudioEvent event) {
		for(ModuleEventAdapter listener : this.listeners)
			try {
				listener.onEvent(event);
			} catch(Exception e) {
				Logger.fatal("EventManager", "Error executing audio event", e);
			}
	}

	public void onEvent(Event event) {
		for(ModuleEventAdapter listener : this.listeners)
			try {
				listener.onEvent(event);
			} catch(Exception e) {
				Logger.fatal("EventManager", "Error executing jda event", e);
			}
	}

	public void addListener(ModuleEventAdapter eventAdapter) {
		this.listeners.add(eventAdapter);
	}

	public void removeListener(ModuleEventAdapter eventAdapter) {
		this.listeners.remove(eventAdapter);
	}

	public WuffyAudioGuild getAudioGuild() {
		return this.audioGuild;
	}
}