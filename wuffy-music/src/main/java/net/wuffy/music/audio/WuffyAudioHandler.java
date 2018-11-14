package net.wuffy.music.audio;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.wuffy.common.util.ITickable;
import net.wuffy.music.audio.WuffyAudioQueue.EnumAudioQueueType;
import net.wuffy.network.NetworkManager;

public class WuffyAudioHandler implements ITickable {

	private static final AudioPlayerManager AUDIO_PLAYER_MANAGER = new DefaultAudioPlayerManager();

	private static final Map<Long, WuffyAudioGuild> GUILDS = new HashMap<Long, WuffyAudioGuild>();

	static {
		AudioSourceManagers.registerRemoteSources(WuffyAudioHandler.AUDIO_PLAYER_MANAGER);
		AudioSourceManagers.registerLocalSource(WuffyAudioHandler.AUDIO_PLAYER_MANAGER);
	}

	public static WuffyAudioGuild getGuild(long guildId) {
		return WuffyAudioHandler.GUILDS.get(guildId);
	}

	public static void addGuild(NetworkManager networkManager, Guild guild, TextChannel textChannel, VoiceChannel voiceChannel, EnumAudioQueueType queueType) {
		WuffyAudioHandler.GUILDS.put(guild.getIdLong(), new WuffyAudioGuild(networkManager, WuffyAudioHandler.AUDIO_PLAYER_MANAGER.createPlayer(), guild, textChannel, voiceChannel, queueType));
	}

	public static void removeGuild(long guildId) {
		WuffyAudioGuild audioGuild = WuffyAudioHandler.GUILDS.remove(guildId);

		if(audioGuild != null)
			audioGuild.destroy();
	}

	public static AudioPlayerManager getAudioPlayerManager() {
		return WuffyAudioHandler.AUDIO_PLAYER_MANAGER;
	}

	public int ticks = 0;

	@Override
	public void update() {
		if(++ticks > 8) {
			ticks = 0;

			GUILDS.values().forEach(ITickable::update);
		}
	}
}