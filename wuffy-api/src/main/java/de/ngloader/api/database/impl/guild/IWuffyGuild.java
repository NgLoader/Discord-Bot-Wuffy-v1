package de.ngloader.api.database.impl.guild;

import java.util.List;

import net.dv8tion.jda.core.entities.Guild;

public interface IWuffyGuild extends Guild {

	public GuildSettings getSettings();

	public boolean isBlocked();

	public boolean setBlocked(Boolean blocked, String reason, Long expire);

	public String getLocale();

	public void setLocale(String locale);

	public void createPlaylist(GuildMusicPlaylist playlist);

	public List<GuildMusicPlaylist> getPlaylists();

	public GuildMusicPlaylist getPlaylist(String name);

	public void deletePlaylist(GuildMusicPlaylist playlist);
}