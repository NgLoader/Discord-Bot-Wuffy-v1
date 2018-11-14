package net.wuffy.music.audio.listener;

import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.AudioChannel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import net.wuffy.common.util.TimeUtil;
import net.wuffy.common.util.TimeUtil.EnumDisplayType;
import net.wuffy.music.audio.WuffyAudioGuild;
import net.wuffy.music.audio.WuffyAudioQueue;
import net.wuffy.music.audio.module.ModuleEventAdapter;

public class MessagePrintListener extends ModuleEventAdapter {

	private static final String EMOJI_LIKE = "👍";
	private static final String EMOJI_DISLIKE = "👎";
	private static final String EMOJI_SPACE = ":__:511640176120954881";
	private static final String EMOJI_SPACE_2 = ":___:511950277491032092";
	private static final String EMOJI_SKIP_BACK = "⬅";
	private static final String EMOJI_SKIP_NEXT = "➡";
	private static final String EMOJI_PLAY_PAUSE = "⏯";
	private static final String EMOJI_VOLUME_UP = "🔼";
	private static final String EMOJI_VOLUME_DOWN = "🔽";
	private static final String EMOJI_STOP = "⏹";

	private static final double GRAPH_LENGTH = 16;

	private WuffyAudioGuild audioGuild;

	public AudioTrack track = null;

	public Message lastMessage = null;
	public int oldGraphStep = 0;

	public long lastPrint = 0;

	public Map<Long, MessageReaction> userVotes = new HashMap<Long, MessageReaction>();
	public int voteSkip = 0;

	public MessagePrintListener(WuffyAudioGuild audioGuild) {
		this.audioGuild = audioGuild;
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		TextChannel textChannel = this.getTextChannel();

		if(this.getTextChannel() == null)
			return;

		this.track = track;
		this.oldGraphStep = 0;
		this.voteSkip = 0;
		this.userVotes.clear();

		this.print(textChannel, this.track, true, true, true);
	}

	@Override
	public void onUpdate() {
		try {
			TextChannel textChannel;
			if(this.track != null && (textChannel = this.getTextChannel()) != null) {
				int grapCurrentlyStep = (int) ((MessagePrintListener.GRAPH_LENGTH / this.track.getDuration()) * this.track.getPosition());

				if(this.oldGraphStep != grapCurrentlyStep || this.lastPrint < System.currentTimeMillis() - 30000) {
					this.oldGraphStep = grapCurrentlyStep;

					this.print(textChannel, this.track, false, false, false);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(this.lastMessage != null && !event.getUser().isBot() && this.lastMessage.getIdLong() == event.getMessageIdLong()) {
			AudioChannel memberAudioChannel = event.getMember().getVoiceState().getAudioChannel();
			VoiceChannel selfAudioChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();

			if(memberAudioChannel == null || selfAudioChannel == null || memberAudioChannel.getIdLong() != selfAudioChannel.getIdLong()) {
				event.getReaction().removeReaction(event.getUser()).queue();
				return;
			}

			if(this.userVotes.containsKey(event.getUser().getIdLong()))
				this.userVotes.get(event.getUser().getIdLong()).removeReaction(event.getUser()).queue();
			this.userVotes.put(event.getUser().getIdLong(), event.getReaction());

			String emote = event.getReaction().getReactionEmote().getName();

			if(event.getReactionEmote().getName().equals(MessagePrintListener.EMOJI_DISLIKE))
				this.changeVoteSkip(event.getUser(), true, (int) selfAudioChannel.getMembers().stream().filter(member -> !member.getUser().isBot()).count());
			else if(event.getReactionEmote().getName().equals(MessagePrintListener.EMOJI_LIKE))
				this.changeVoteSkip(event.getUser(), false, (int) selfAudioChannel.getMembers().stream().filter(member -> !member.getUser().isBot()).count());
			else {
				WuffyAudioQueue audioQueue = this.audioGuild.getAudioQueue();

				event.getReaction().removeReaction(event.getUser()).queue();

				switch(emote) {
					case MessagePrintListener.EMOJI_SKIP_BACK:
						audioQueue.play(audioQueue.getLast());
						break;
		
					case MessagePrintListener.EMOJI_SKIP_NEXT:
						audioQueue.play(audioQueue.getNext(true));
						break;
		
					case MessagePrintListener.EMOJI_PLAY_PAUSE:
						this.audioGuild.getAudioPlayer().setPaused(!this.audioGuild.getAudioPlayer().isPaused());
						break;

					case MessagePrintListener.EMOJI_STOP:
						if(!this.audioGuild.isDestroyed())
							this.audioGuild.destroy();
						break;

					case MessagePrintListener.EMOJI_VOLUME_UP:
						int volume = this.audioGuild.getAudioPlayer().getVolume() + 5;

						if(volume < 101)
							this.audioGuild.getAudioPlayer().setVolume(volume);
						break;

					case MessagePrintListener.EMOJI_VOLUME_DOWN:
						volume = this.audioGuild.getAudioPlayer().getVolume() - 5;

						if(volume > 0)
							this.audioGuild.getAudioPlayer().setVolume(volume);
						break;

					default:
						break;
				}
			}
		}
	}

	@Override
	public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
		if(this.lastMessage != null && !event.getUser().isBot() && this.lastMessage.getIdLong() == event.getMessageIdLong()) {
			AudioChannel memberAudioChannel = event.getMember().getVoiceState().getAudioChannel();
			VoiceChannel selfAudioChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();

			if(memberAudioChannel == null || selfAudioChannel == null || memberAudioChannel.getIdLong() != selfAudioChannel.getIdLong())
				return;

			if(this.userVotes.containsKey(event.getUser().getIdLong()) && this.userVotes.get(event.getUser().getIdLong()).getReactionEmote().getName().equals(event.getReactionEmote().getName()))
				this.userVotes.remove(event.getUser().getIdLong());

			String emote = event.getReaction().getReactionEmote().getName();

			if(emote.equals(MessagePrintListener.EMOJI_DISLIKE))
				this.changeVoteSkip(event.getUser(), false, (int) selfAudioChannel.getMembers().stream().filter(member -> !member.getUser().isBot()).count());
			else if(emote.equals(MessagePrintListener.EMOJI_LIKE))
				this.changeVoteSkip(event.getUser(), true, (int) selfAudioChannel.getMembers().stream().filter(member -> !member.getUser().isBot()).count());
		}
	}

	@Override
	public void onGuildMessageReactionRemoveAll(GuildMessageReactionRemoveAllEvent event) {
		if(this.lastMessage != null && this.lastMessage.getIdLong() == event.getMessageIdLong()) {
			this.voteSkip = 0;
			this.userVotes.clear();

			this.lastMessage.addReaction(MessagePrintListener.EMOJI_LIKE).complete();
			this.lastMessage.addReaction(MessagePrintListener.EMOJI_DISLIKE).complete();
		}
	}

	public void changeVoteSkip(User user, boolean skip, int voiceChannelCount) {
		if(skip)
			this.voteSkip++;
		else
			this.voteSkip--;

		if(this.voteSkip > this.getVoteSkipCount(voiceChannelCount))
			this.audioGuild.getAudioQueue().play(this.audioGuild.getAudioQueue().skipTo(1));
	}

	public int getVoteSkipCount(int memberCount) {
		if(memberCount == 1)
			return 0;
		if(memberCount == 2)
			return 1;

		return (int) memberCount / 100 * 50;
	}

	public void print(TextChannel textChannel, AudioTrack track, boolean newEmbed, boolean checkIsLastMessage, boolean resetEmotes) {
		this.lastPrint = System.currentTimeMillis();

		if(this.lastMessage != null) {
			if(!checkIsLastMessage || !textChannel.hasLatestMessage() || this.lastMessage.getIdLong() == textChannel.getLatestMessageIdLong()) {
				MessageAction messageAction = this.lastMessage.editMessage(this.buildMessage(track));

				if(newEmbed)
					messageAction.embed(this.buildEmbed(this.track).build());

				if(resetEmotes) {
					Message message = messageAction.complete();

					message.clearReactions().complete();
					message.addReaction(MessagePrintListener.EMOJI_LIKE).complete();
					message.addReaction(MessagePrintListener.EMOJI_DISLIKE).complete();
					message.addReaction(MessagePrintListener.EMOJI_SPACE).complete();
					message.addReaction(MessagePrintListener.EMOJI_SKIP_BACK).complete();
					message.addReaction(MessagePrintListener.EMOJI_STOP).complete();
					message.addReaction(MessagePrintListener.EMOJI_PLAY_PAUSE).complete();
					message.addReaction(MessagePrintListener.EMOJI_SKIP_NEXT).complete();
					message.addReaction(MessagePrintListener.EMOJI_SPACE_2).complete();
					message.addReaction(MessagePrintListener.EMOJI_VOLUME_UP).complete();
					message.addReaction(MessagePrintListener.EMOJI_VOLUME_DOWN).complete();
				} else
					messageAction.queue();
				return;
			} else
				this.lastMessage.delete().queue();
		}

		this.lastMessage = textChannel.sendMessage(this.buildMessage(track)).embed(this.buildEmbed(this.track).build()).complete();

		this.lastMessage.addReaction(MessagePrintListener.EMOJI_LIKE).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_DISLIKE).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_SPACE).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_SKIP_BACK).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_STOP).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_PLAY_PAUSE).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_SKIP_NEXT).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_SPACE_2).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_VOLUME_UP).complete();
		this.lastMessage.addReaction(MessagePrintListener.EMOJI_VOLUME_DOWN).complete();
	}

	public String buildMessage(AudioTrack track) {
		return String.format("%s %s %s",
				new TimeUtil(this.track.getPosition(), EnumSet.range(EnumDisplayType.SECOND, EnumDisplayType.DAY)).format("%h:%min:%sec", true),
				this.buildGraph(track),
				new TimeUtil(this.track.getDuration(), EnumSet.range(EnumDisplayType.SECOND, EnumDisplayType.DAY)).format("%h:%min:%sec", true));
	}

	public String buildGraph(AudioTrack track) {
		int grapCurrentlyStep = (int) ((MessagePrintListener.GRAPH_LENGTH / track.getDuration()) * track.getPosition());

		String graph = "";
		for (int i = 0; i < MessagePrintListener.GRAPH_LENGTH; i++)
			graph += i == grapCurrentlyStep ? "🔘" : "▬";

		return graph;
	}

	public EmbedBuilder buildEmbed(AudioTrack audioTrack) {
		return new EmbedBuilder()
				.setTitle(audioTrack.getInfo().title, audioTrack.getInfo().uri)
				.setImage(String.format("https://img.youtube.com/vi/%s/maxresdefault.jpg", audioTrack.getInfo().uri.replaceAll(".*(?:youtu.be\\\\/|v\\\\/|u/\\\\w/|embed\\\\/|watch\\\\?.*&?v=)", "")))
				.setColor(Color.getHSBColor(51.2F, 92.3F, 81.2F));
	}

	public TextChannel getTextChannel() {
		TextChannel textChannel = this.audioGuild.getTextChannel();

		if(textChannel != null && textChannel.getGuild().getSelfMember().hasPermission(textChannel,
				Permission.MESSAGE_EMBED_LINKS,
				Permission.MESSAGE_READ,
				Permission.MESSAGE_WRITE,
				Permission.MESSAGE_MANAGE,
				Permission.MESSAGE_ADD_REACTION,
				Permission.MESSAGE_EXT_EMOJI))
			return textChannel;
		return null;
	}

	@Override
	public void onDestroy() {
		if(this.lastMessage != null)
			this.lastMessage.delete().complete();
	}
}