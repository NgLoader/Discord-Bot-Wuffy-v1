package de.ngloader.api.database.impl.guild;

import java.util.List;

import net.dv8tion.jda.core.entities.Guild;

public interface IWuffyGuild extends Guild {

	public GuildSettings getSettings();

	public void createPlaylist(GuildMusicPlaylist playlist);

	public List<GuildMusicPlaylist> getPlaylists();

	public void deletePlaylist(GuildMusicPlaylist playlist);
}