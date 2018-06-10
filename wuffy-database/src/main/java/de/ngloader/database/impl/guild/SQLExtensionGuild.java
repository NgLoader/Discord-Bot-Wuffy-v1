package de.ngloader.database.impl.guild;

import java.util.List;

import de.ngloader.api.database.IStorageProvider;
import de.ngloader.api.database.impl.guild.GuildMusicPlaylist;
import de.ngloader.api.database.impl.guild.GuildSettings;
import de.ngloader.api.database.impl.guild.IExtensionGuild;
import de.ngloader.api.database.sql.SQLStorage;

public class SQLExtensionGuild implements IStorageProvider<SQLStorage>, IExtensionGuild {

	@Override
	public void registered(SQLStorage storage) {
	}

	@Override
	public GuildSettings getGuildSettings(long longId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGuildSettings(long longId, GuildSettings guildConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPlaylist(long longId, GuildMusicPlaylist playlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<GuildMusicPlaylist> getPlaylists(long longId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePlaylist(long longId, GuildMusicPlaylist playlist) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GuildMusicPlaylist getPlaylist(long longId, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePlaylist(long longId, GuildMusicPlaylist playlist) {
		// TODO Auto-generated method stub
		
	}
}