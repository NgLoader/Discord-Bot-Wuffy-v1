package de.ngloader.core.database.impl.guild;

import org.bson.types.ObjectId;

import com.mongodb.lang.NonNull;

public class GuildConfig {

	public ObjectId _id;

	@NonNull
	public long guildId;

	public GuildSettings settings = new GuildSettings();
	public GuildMusicPlaylist musicPlaylist = new GuildMusicPlaylist();

	public GuildConfig(long guildId) {
		this.guildId = guildId;
	}
}