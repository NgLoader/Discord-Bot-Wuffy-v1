package net.wuffy.music.audio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.sedmelluq.discord.lavaplayer.tools.io.MessageInput;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class WuffyAudioQueue {

	private static final Map<byte[], AudioTrack> trackCache = new HashMap<byte[], AudioTrack>();

	private WuffyAudioGuild audioGuild;

	private List<AudioTrack> playlist = new ArrayList<AudioTrack>();
	private Queue<AudioTrack> queue = new LinkedList<AudioTrack>();

	private EnumAudioQueueType queueType;

	private boolean shuffled = false;

	public WuffyAudioQueue(WuffyAudioGuild audioGuild, EnumAudioQueueType queueType) {
		this.audioGuild = audioGuild;
		this.queueType = queueType;
	}

	public void destroy() {
		this.playlist.clear();
		this.queue.clear();

		this.audioGuild = null;
		this.playlist = null;
		this.queue = null;
		this.queueType = null;
	}

	public void setPlaylist(List<byte[]> buffers) throws IOException {
		this.playlist.clear();
		this.queue.clear();

		this.addPlaylist(buffers);

		this.audioGuild.getAudioPlayer().stopTrack();
	}

	public void addPlaylist(List<byte[]> buffers) throws IOException {
		for(byte[] buffer : buffers) {
			if(!WuffyAudioQueue.trackCache.containsKey(buffer))
				WuffyAudioQueue.trackCache.put(buffer, WuffyAudioHandler.getAudioPlayerManager().decodeTrack(new MessageInput(new ByteArrayInputStream(buffer))).decodedTrack);

			AudioTrack audioTrack = WuffyAudioQueue.trackCache.get(buffer);

			this.playlist.add(audioTrack);
			this.queue.add(audioTrack);
		}
	}

	public void removePlaylist(List<byte[]> buffers) {
		for(byte[] buffer : buffers) {
			AudioTrack audioTrack = WuffyAudioQueue.trackCache.get(buffer);

			if(audioTrack != null) {
				this.playlist.remove(audioTrack);
				this.queue.remove(audioTrack);
			}
		}
	}

	public void play(AudioTrack audioTrack) {
		if(!this.audioGuild.isDestroyed())
			this.getAudioGuild().getAudioPlayer().playTrack(audioTrack.makeClone());
	}

	public void reset() {
		this.queue.clear();
		this.queue.addAll(this.playlist);
	}

	public AudioTrack getNext(boolean ignoreLoop) {
		AudioTrack track = this.queue.isEmpty() ? null : this.queue.poll();

		if(track == null) {
			if(this.shuffled) {
				List<AudioTrack> playlistCopy = new ArrayList<AudioTrack>(this.playlist);

				Collections.shuffle(playlistCopy);

				this.queue.addAll(playlistCopy);
			} else
				this.queue.addAll(this.playlist);

			switch (this.queueType) {
				case LOOP:
					if(ignoreLoop)
						track = this.queue.poll();
					else
						track = this.queue.peek();
					break;
	
				case REPEAT:
					track = this.queue.poll();
					break;
	
				case STOP:
					break;
	
				default:
					break;
			}
		}

		return track;
	}

	public AudioTrack getLast() {
		List<AudioTrack> oldQueue = new LinkedList<AudioTrack>(this.queue);

		this.queue.clear();

		this.queue.add(this.playlist.get(this.playlist.size() - this.queue.size() - 1));
		this.queue.addAll(oldQueue);

		return this.queue.poll();
	}

	public AudioTrack skipTo(int skip) {
		AudioTrack track = null;

		while(skip-- > 0)
			track = this.getNext(true);

		return track;
	}

	public void setShuffled(boolean shuffled) {
		this.shuffled = shuffled;

		List<AudioTrack> queueCopy = new ArrayList<AudioTrack>(this.queue);

		if(this.shuffled)
			Collections.shuffle(queueCopy);
		else {
			List<AudioTrack> sorted = new ArrayList<AudioTrack>();

			for(AudioTrack audioTrack : this.playlist)
				if(queueCopy.contains(audioTrack))
					sorted.add(audioTrack);

			queueCopy = sorted;
		}

		this.queue.clear();
		this.queue.addAll(queueCopy);
	}

	public AudioTrack getCurrentlyTrack() {
		return this.audioGuild.getAudioPlayer().getPlayingTrack();
	}

	public WuffyAudioGuild getAudioGuild() {
		return this.audioGuild;
	}

	public EnumAudioQueueType getQueueType() {
		return this.queueType;
	}

	public void setQueueType(EnumAudioQueueType queueType) {
		this.queueType = queueType;
	}

	public boolean isShuffled() {
		return this.shuffled;
	}

	public enum EnumAudioQueueType {
		LOOP, //Loop the currently song
		REPEAT, //Repeat playlist
		STOP //Stop by queue empty
	}
}