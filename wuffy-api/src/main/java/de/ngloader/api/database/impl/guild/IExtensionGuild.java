package de.ngloader.api.database.impl.guild;

import java.util.List;

import de.ngloader.api.database.IStorageExtension;

public interface IExtensionGuild extends IStorageExtension {

	GuildSettings getGuildSettings(long longId);

	void setGuildSettings(long longId, GuildSettings guildConfig);

	/* Music */
	void createPlaylist(long longId, GuildMusicPlaylist playlist);

	List<GuildMusicPlaylist> getPlaylists(long longId);

	void updatePlaylist(long longId, GuildMusicPlaylist playlist);

	void deletePlaylist(long longId, GuildMusicPlaylist playlist);
}