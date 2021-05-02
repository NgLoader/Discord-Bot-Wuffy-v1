package net.wuffy.music.audio.module;

import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;

import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.wuffy.music.audio.module.event.DestroyEvent;
import net.wuffy.music.audio.module.event.UpdateEvent;

public abstract class ModuleEventAdapter extends AudioEventAdapter {

	//Module
	public void onDestroy() { }
	public void onUpdate() { }

	//Guild Voice
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) { }
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) { }
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) { }

	public void onEvent(ModuleEvent event) {
		if(event instanceof UpdateEvent)
			this.onUpdate();
		else if(event instanceof DestroyEvent)
			this.onDestroy();
	}

	@Override
	public void onEvent(AudioEvent event) {
		super.onEvent(event);
	}

	public void onEvent(Event event) {
		if(event instanceof GuildVoiceJoinEvent)
			this.onGuildVoiceJoin((GuildVoiceJoinEvent) event);
		else if(event instanceof GuildVoiceLeaveEvent)
			this.onGuildVoiceLeave((GuildVoiceLeaveEvent) event);
		else if(event instanceof GuildVoiceMoveEvent)
			this.onGuildVoiceMove((GuildVoiceMoveEvent) event);
	}
}