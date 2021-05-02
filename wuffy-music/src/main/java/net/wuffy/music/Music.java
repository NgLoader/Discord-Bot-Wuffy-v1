package net.wuffy.music;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageOutput;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.wuffy.common.config.ConfigService;
import net.wuffy.common.logger.Logger;
import net.wuffy.common.logger.LoggerManager;
import net.wuffy.common.util.ITickable;
import net.wuffy.common.util.TickingTask;
import net.wuffy.music.audio.WuffyAudioGuild;
import net.wuffy.music.audio.WuffyAudioHandler;
import net.wuffy.music.audio.WuffyAudioQueue.EnumAudioQueueType;
import net.wuffy.music.network.NetworkSystem;

public class Music extends TickingTask {

	private static Music instance;

	public static Music getInstance() {
		return Music.instance;
	}

	public static void main(String[] args) {
		if(Float.parseFloat(System.getProperty( "java.class.version" )) < 54) {
			System.err.println("*** ERROR *** Wuffy equires Java 10 or above to work! Please download and install it!");
			return;
		}

		Logger.info("Bootstrap", "Starting Wuffy Master.");

		var threads = 32;

		for(int i = 0; i < args.length; i++) {
			var arg = args[i];

			if(arg.equalsIgnoreCase("--debug"))
				LoggerManager.setDebug(true);
			else if(arg.equalsIgnoreCase("--threads"))
				threads = Integer.valueOf(args[i + 1]);
			else if(arg.equalsIgnoreCase("--dev"))
				System.setProperty("developerMode", "true");
		}

		System.setProperty("io.netty.eventLoopThreads", Integer.toString(threads));

		Logger.info("Bootstrap", "Loading");

		Music.instance = new Music();
	}

	private final MusicConfig config;

	private List<ITickable> tickables = new ArrayList<ITickable>();

	private NetworkSystem networkSystem;

	private JDAHandler jdaHandler;

	public Music() {
		super(1000 / 2);

		this.config = ConfigService.getConfig(MusicConfig.class);

		this.jdaHandler = new JDAHandler("NDcyNTc0NzIzNzY3OTI2Nzg0.DrrI4A.TolqbuOSgSMhVNB_w5knhaj987I", 4);

		this.networkSystem = new NetworkSystem();
//		try {
//			this.networkSystem.start(this.config);
//		} catch (SSLException e) {
//			Logger.fatal("Bootstrap", "SSLException", e);
//		}

		this.tickables.add(this.jdaHandler);
		this.tickables.add(this.networkSystem);
		this.tickables.add(new WuffyAudioHandler());

		//Start main thread
		new Thread(this, "Wuffy Master").start();
	}

	int test = 0;

	@Override
	protected void update() {
		this.tickables.forEach(ITickable::update);

		try {
			if(++test == 8) {
				System.out.println("TEST");
				Guild guild = this.jdaHandler.getGuild(330812207598272515L);

				WuffyAudioHandler.addGuild(null, guild, guild.getTextChannelById(343187634388205568L), guild.getVoiceChannels().get(0), EnumAudioQueueType.REPEAT);

				WuffyAudioGuild audioGuild = WuffyAudioHandler.getGuild(guild.getIdLong());

				List<byte[]> buffers = new ArrayList<byte[]>();

				WuffyAudioHandler.getAudioPlayerManager().loadItem("https://www.youtube.com/playlist?list=PLtkmSM7joY7vK6Xy7FjWv8w7rdbd579jP", new AudioLoadResultHandler() {
					
					@Override
					public void trackLoaded(AudioTrack track) {
						ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
						try {
							WuffyAudioHandler.getAudioPlayerManager().encodeTrack(new MessageOutput(byteArrayOutputStream), track);
						} catch (IOException e) {
							e.printStackTrace();
						}
						buffers.add(byteArrayOutputStream.toByteArray());

						try {
							audioGuild.getAudioQueue().addPlaylist(buffers);
						} catch (IOException e) {
							e.printStackTrace();
						}

						audioGuild.getAudioQueue().play(audioGuild.getAudioQueue().getNext(true));
					}
					
					@Override
					public void playlistLoaded(AudioPlaylist playlist) {
						for(AudioTrack audioTrack : playlist.getTracks()) {
							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							try {
								WuffyAudioHandler.getAudioPlayerManager().encodeTrack(new MessageOutput(byteArrayOutputStream), audioTrack);
							} catch (IOException e) {
								e.printStackTrace();
							}
							buffers.add(byteArrayOutputStream.toByteArray());
						}

						try {
							audioGuild.getAudioQueue().addPlaylist(buffers);
						} catch (IOException e) {
							e.printStackTrace();
						}

						audioGuild.getAudioQueue().setShuffled(true);

						audioGuild.getAudioQueue().play(audioGuild.getAudioQueue().getNext(true));
					}
					
					@Override
					public void noMatches() { }
					
					@Override
					public void loadFailed(FriendlyException exception) { }
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void stop() {
	}

	public JDAHandler getJdaHandler() {
		return this.jdaHandler;
	}

	public NetworkSystem getNetworkSystem() {
		return this.networkSystem;
	}

	public MusicConfig getConfig() {
		return this.config;
	}
}