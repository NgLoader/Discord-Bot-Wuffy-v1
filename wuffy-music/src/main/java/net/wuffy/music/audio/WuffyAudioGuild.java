package net.wuffy.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.core.audio.AudioSendHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.wuffy.common.WuffyPhantomRefernce;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.IWuffyPhantomReference;
import net.wuffy.music.audio.WuffyAudioQueue.EnumAudioQueueType;
import net.wuffy.music.audio.listener.AudioEventListener;
import net.wuffy.music.audio.listener.MessagePrintListener;
import net.wuffy.music.audio.module.ModuleEventManager;
import net.wuffy.music.audio.module.event.UpdateEvent;
import net.wuffy.network.NetworkManager;

public class WuffyAudioGuild implements IWuffyPhantomReference, ITickable {

	private AudioSendHandler audioSendHandler;
	private NetworkManager networkManager;
	private AudioPlayer audioPlayer;
	private Guild guild;

	private ModuleEventManager eventManager;

	private TextChannel textChannel;

	private WuffyAudioQueue audioQueue;

	private boolean destroyed = false;

	public WuffyAudioGuild(NetworkManager networkManager, AudioPlayer audioPlayer, Guild guild, long textChannelId, long voiceChannelId, EnumAudioQueueType queueType) {
		this(networkManager, audioPlayer, guild, guild.getTextChannelById(textChannelId), guild.getVoiceChannelById(voiceChannelId), queueType);
	}

	public WuffyAudioGuild(NetworkManager networkManager, AudioPlayer audioPlayer, Guild guild, TextChannel textChannel, VoiceChannel voiceChannel, EnumAudioQueueType queueType) {
		this.networkManager = networkManager;
		this.audioPlayer = audioPlayer;
		this.guild = guild;
		this.textChannel = textChannel;

		this.audioSendHandler = new WuffyAudioSendHandler(this.audioPlayer);

		this.audioQueue = new WuffyAudioQueue(this, queueType);

		this.eventManager = new ModuleEventManager(this);
		this.eventManager.addListener(new AudioEventListener(this));
		this.eventManager.addListener(new MessagePrintListener(this));

		this.audioPlayer.addListener(new WuffyAudioEventAdapter(this.eventManager));
		this.audioPlayer.setVolume(10);

		this.guild.getAudioManager().setSendingHandler(this.audioSendHandler);
		this.guild.getAudioManager().setAutoReconnect(true);

		if(voiceChannel != null)
			this.guild.getAudioManager().openAudioConnection(voiceChannel);

		WuffyPhantomRefernce.getInstance().add(this, String.format("WuffyAudioGuild - ", Long.toString(this.guild.getIdLong())));
	}

	@Override
	public void update() {
		this.eventManager.onEvent(new UpdateEvent());
	}

	public void destroy() {
		this.destroyed = true;

		this.audioPlayer.destroy();
		this.guild.getAudioManager().closeAudioConnection();

		this.audioSendHandler = null;
		this.networkManager = null;
		this.audioPlayer = null;
		this.guild = null;
		this.textChannel = null;
		this.audioPlayer = null;
	}

	public AudioSendHandler getAudioSendHandler() {
		return this.audioSendHandler;
	}

	public NetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public AudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}

	public Guild getGuild() {
		return this.guild;
	}

	public ModuleEventManager getEventManager() {
		return this.eventManager;
	}

	public TextChannel getTextChannel() {
		return this.textChannel;
	}

	public void setTextChannel(TextChannel textChannel) {
		this.textChannel = textChannel;
	}

	public WuffyAudioQueue getAudioQueue() {
		return this.audioQueue;
	}

	public boolean isDestroyed() {
		return this.destroyed;
	}
}