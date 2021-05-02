package net.wuffy.music.audio.listener;

import java.awt.Color;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.wuffy.music.audio.WuffyAudioGuild;
import net.wuffy.music.audio.module.ModuleEventAdapter;

public class MessagePrintListener extends ModuleEventAdapter {

	private WuffyAudioGuild audioGuild;

	public Message lastMessage = null;
	public AudioTrack track = null;
	public boolean printNewEmbed = false;

	public MessagePrintListener(WuffyAudioGuild audioGuild) {
		this.audioGuild = audioGuild;
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		TextChannel textChannel = this.audioGuild.getTextChannel();

		if(textChannel == null)
			return;

		if(!textChannel.canTalk()) //TODO check has embed perms
			return;

		this.track = track;
		this.printNewEmbed = true;
	}

	@Override
	public void onUpdate() {
		if(this.track != null) {
			TextChannel textChannel = this.audioGuild.getTextChannel();

			if(textChannel == null)
				return;

			double graphLength = 16;
			int grapCurrentlyStep = (int) ((graphLength / track.getDuration()) * track.getPosition());

			String graph = "";
			for (int i = 0; i < graphLength; i++)
				graph += i == grapCurrentlyStep ? "🔘" : "▬";

			try {
				if(this.printNewEmbed) {
					this.printNewEmbed = false;

					if(this.lastMessage != null)
						this.lastMessage.editMessage(graph).embed(this.buildEmbed(this.track).build()).queue();
					else
						this.lastMessage = textChannel.sendMessage(graph).embed(this.buildEmbed(this.track).build()).complete();
					return;
				}

				if(this.lastMessage != null) {
					if(this.lastMessage.getContentRaw().equals(graph))
						return;

					this.lastMessage.editMessage(graph).queue();
				} else
					this.lastMessage = textChannel.sendMessage(graph).embed(this.buildEmbed(this.track).build()).complete();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public EmbedBuilder buildEmbed(AudioTrack audioTrack) {
		return new EmbedBuilder()
				.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
				.setImage(String.format("https://img.youtube.com/vi/%s/default.jpg", audioTrack.getInfo().uri.replaceAll(".*(?:youtu.be\\\\/|v\\\\/|u/\\\\w/|embed\\\\/|watch\\\\?.*&?v=)", "")))
				.setColor(Color.CYAN);
	}

	@Override
	public void onDestroy() {
		if(this.lastMessage != null)
			this.lastMessage.delete().complete();
	}
}