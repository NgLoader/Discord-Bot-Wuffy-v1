package net.wuffy.music.audio.listener;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.wuffy.music.audio.WuffyAudioGuild;
import net.wuffy.music.audio.WuffyAudioQueue;
import net.wuffy.music.audio.module.ModuleEventAdapter;

public class AudioEventListener extends ModuleEventAdapter {

	private WuffyAudioGuild audioGuild;

	private boolean resumeAudioWhenVoiceJoin = false;

	public AudioEventListener(WuffyAudioGuild audioGuild) {
		this.audioGuild = audioGuild;
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if(endReason == AudioTrackEndReason.REPLACED || this.audioGuild.isDestroyed())
			return;

		WuffyAudioQueue audioQueue = this.audioGuild.getAudioQueue();
		AudioTrack audioTrack = audioQueue.getNext(false);

		if(audioTrack != null)
			audioQueue.play(audioTrack);
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
	}

	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		if(this.resumeAudioWhenVoiceJoin) {
			VoiceChannel connectedChannel = this.audioGuild.getGuild().getAudioManager().getConnectedChannel();

			if(connectedChannel != null && connectedChannel.getIdLong() == event.getChannelJoined().getIdLong() && this.audioGuild.getAudioPlayer().isPaused()) {
				this.audioGuild.getAudioPlayer().setPaused(false);
				this.resumeAudioWhenVoiceJoin = false;
			}
		}
	}

	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		if(!this.resumeAudioWhenVoiceJoin && !event.getChannelLeft().getMembers().stream().anyMatch(member -> !member.getUser().isBot())) {
			VoiceChannel connectedChannel = this.audioGuild.getGuild().getAudioManager().getConnectedChannel();

			if(connectedChannel != null && connectedChannel.getIdLong() == event.getChannelLeft().getIdLong() && !this.audioGuild.getAudioPlayer().isPaused()) {
				this.audioGuild.getAudioPlayer().setPaused(true);
				this.resumeAudioWhenVoiceJoin = true;
			}
		}
	}

	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		VoiceChannel connectedChannel = this.audioGuild.getGuild().getAudioManager().getConnectedChannel();

		if(connectedChannel == null)
			return;

		if(connectedChannel.getIdLong() == event.getChannelJoined().getIdLong())
			if(!event.getChannelJoined().getMembers().stream().anyMatch(member -> !member.getUser().isBot()) && !this.resumeAudioWhenVoiceJoin) {
				this.audioGuild.getAudioPlayer().setPaused(true);
				this.resumeAudioWhenVoiceJoin = true;
			} else if(this.resumeAudioWhenVoiceJoin) {
				this.audioGuild.getAudioPlayer().setPaused(false);
				this.resumeAudioWhenVoiceJoin = false;
			}
	}
}