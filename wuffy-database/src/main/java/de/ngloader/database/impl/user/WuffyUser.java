package de.ngloader.database.impl.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ngloader.api.database.impl.user.IWuffyUser;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.requests.RestAction;

public class WuffyUser implements IWuffyUser {

	private static final Map<Long, WuffyUser> CACHE = new HashMap<Long, WuffyUser>();

	public static WuffyUser getUser(User user) {
		var longId = user.getIdLong();

		if(!CACHE.containsKey(longId)) {
			CACHE.put(longId, new WuffyUser(user));
		}

		return CACHE.get(longId);
	}

	private final User user;

	public WuffyUser(User user) {
		if(user == null)
			throw new NullPointerException("User is null");

		this.user = user;
	}

	@Override
	public long getIdLong() {
		return this.user.getIdLong();
	}

	@Override
	public String getAsMention() {
		return this.user.getAsMention();
	}

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
}