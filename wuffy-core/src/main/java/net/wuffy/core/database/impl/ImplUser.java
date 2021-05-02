package net.wuffy.core.database.impl;

import java.util.EnumSet;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import net.wuffy.core.Core;

public class ImplUser implements User {

	protected final Core core;
	protected final User user;

	public ImplUser(Core core, User user) {
		this.core = core;
		this.user = user;
	}

	public Core getCore() {
		return core;
	}

	@Override
	public long getIdLong() {
		return this.user.getIdLong();
	}

	@Override
	public String getAsMention() {
		return this.user.getAsMention();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFake() {
		return this.user.isFake();
	}

	@Override
	public String getName() {
		return this.user.getName();
	}

	@Override
	public String getDiscriminator() {
		return this.user.getDiscriminator();
	}

	@Override
	public String getAvatarId() {
		return this.user.getAvatarId();
	}

	@Override
	public String getAvatarUrl() {
		return this.user.getAvatarUrl();
	}

	@Override
	public String getDefaultAvatarId() {
		return this.user.getDefaultAvatarId();
	}

	@Override
	public String getDefaultAvatarUrl() {
		return this.user.getDefaultAvatarUrl();
	}

	@Override
	public String getEffectiveAvatarUrl() {
		return this.user.getEffectiveAvatarUrl();
	}

	@Override
	public boolean hasPrivateChannel() {
		return this.user.hasPrivateChannel();
	}

	@Override
	public RestAction<PrivateChannel> openPrivateChannel() {
		return this.user.openPrivateChannel();
	}

	@Override
	public List<Guild> getMutualGuilds() {
		return this.user.getMutualGuilds();
	}

	@Override
	public boolean isBot() {
		return this.user.isBot();
	}

	@Override
	public JDA getJDA() {
		return this.user.getJDA();
	}

	@Override
	public String getAsTag() {
		return this.user.getAsTag();
	}

	@Override
	public boolean isSystem() {
		return this.user.isSystem();
	}

	@Override
	public EnumSet<UserFlag> getFlags() {
		return this.user.getFlags();
	}

	@Override
	public int getFlagsRaw() {
		return this.user.getFlagsRaw();
	}
}