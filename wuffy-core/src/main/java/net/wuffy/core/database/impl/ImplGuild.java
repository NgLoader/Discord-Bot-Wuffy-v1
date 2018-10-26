package net.wuffy.core.database.impl;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Region;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.ListedEmote;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.MemberAction;
import net.dv8tion.jda.core.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.core.utils.cache.MemberCacheView;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;
import net.wuffy.core.Core;

//TODO remove implements Guild and use getGuild
public class ImplGuild implements Guild {

	protected final Core core;
	protected final Guild guild;

	protected final IExtensionGuild<?, ?> extensionGuild;

	public ImplGuild(Core core, Guild guild) {
		this.core = core;
		this.guild = guild;

		this.extensionGuild = this.core.getStorageService().getExtension(IExtensionGuild.class);
	}

	public Core getCore() {
		return this.core;
	}

	public Guild getRealGuild() {
		return this.guild;
	}

	@Override
	public long getIdLong() {
		return this.guild.getIdLong();
	}

	@Override
	public RestAction<EnumSet<Region>> retrieveRegions(boolean includeDeprecated) {
		return this.guild.retrieveRegions(includeDeprecated);
	}

	@Override
	public MemberAction addMember(String accessToken, String userId) {
		return this.guild.addMember(accessToken, userId);
	}

	@Override
	public String getName() {
		return this.guild.getName();
	}

	@Override
	public String getIconId() {
		return this.guild.getIconId();
	}

	@Override
	public String getIconUrl() {
		return this.guild.getIconUrl();
	}

	@Override
	public Set<String> getFeatures() {
		return this.guild.getFeatures();
	}

	@Override
	public String getSplashId() {
		return this.guild.getSplashId();
	}

	@Override
	public String getSplashUrl() {
		return this.guild.getSplashUrl();
	}

	@Override
	public RestAction<String> getVanityUrl() {
		return this.guild.getVanityUrl();
	}

	@Override
	public VoiceChannel getAfkChannel() {
		return this.guild.getAfkChannel();
	}

	@Override
	public TextChannel getSystemChannel() {
		return this.guild.getSystemChannel();
	}

	@Override
	public ImplMember getOwner() {
		return this.extensionGuild.getMemeber(this.guild, this.guild.getOwner());
	}

	@Override
	public Timeout getAfkTimeout() {
		return this.guild.getAfkTimeout();
	}

	@Override
	public String getRegionRaw() {
		return this.guild.getRegionRaw();
	}

	@Override
	public boolean isMember(User user) {
		return this.guild.isMember(user);
	}

	@Override
	public ImplMember getSelfMember() {
		return this.extensionGuild.getMemeber(this.guild, this.guild.getSelfMember());
	}

	@Override
	public ImplMember getMember(User user) {
		return this.extensionGuild.getMemeber(this.guild, this.guild.getMember(user));
	}

	public ImplMember getMember(ImplUser user) {
		return this.extensionGuild.getMemeber(this.guild, this.guild.getMemberById(user.getIdLong()));
	}

	@Override
	public ImplMember getMemberById(long userId) {
		return this.extensionGuild.getMemeber(this.guild, this.guild.getMemberById(userId));
	}

	@Override
	public ImplMember getMemberById(String userId) {
		return this.extensionGuild.getMemeber(this.guild, this.guild.getMemberById(Long.parseLong(userId.replace("<@", "").replace(">", ""))));
	}

	@Override
	public List<Member> getMembers() {
		return this.guild.getMembers();
	}

	@Override
	public MemberCacheView getMemberCache() {
		return this.guild.getMemberCache();
	}

	@Override
	public SnowflakeCacheView<Category> getCategoryCache() {
		return this.guild.getCategoryCache();
	}

	@Override
	public SnowflakeCacheView<TextChannel> getTextChannelCache() {
		return this.guild.getTextChannelCache();
	}

	@Override
	public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
		return this.guild.getVoiceChannelCache();
	}

	@Override
	public SnowflakeCacheView<Role> getRoleCache() {
		return this.guild.getRoleCache();
	}

	@Override
	public SnowflakeCacheView<Emote> getEmoteCache() {
		return this.guild.getEmoteCache();
	}

	@Override
	public RestAction<List<Ban>> getBanList() {
		return this.guild.getBanList();
	}

	@Override
	public RestAction<Integer> getPrunableMemberCount(int days) {
		return this.guild.getPrunableMemberCount(days);
	}

	@Override
	public Role getPublicRole() {
		return this.guild.getPublicRole();
	}

	@Override
	public TextChannel getDefaultChannel() {
		return this.guild.getDefaultChannel();
	}

	@Override
	public GuildManager getManager() {
		return this.guild.getManager();
	}

	@Override
	public GuildController getController() {
		return this.guild.getController();
	}

	@Override
	public MentionPaginationAction getRecentMentions() {
		return this.guild.getRecentMentions();
	}

	@Override
	public AuditLogPaginationAction getAuditLogs() {
		return this.guild.getAuditLogs();
	}

	@Override
	public RestAction<Void> leave() {
		return this.guild.leave();
	}

	@Override
	public RestAction<Void> delete() {
		return this.guild.delete();
	}

	@Override
	public RestAction<Void> delete(String mfaCode) {
		return this.guild.delete(mfaCode);
	}

	@Override
	public AudioManager getAudioManager() {
		return this.guild.getAudioManager();
	}

	@Override
	public JDA getJDA() {
		return this.guild.getJDA();
	}

	@Override
	public RestAction<List<Invite>> getInvites() {
		return this.guild.getInvites();
	}

	@Override
	public RestAction<List<Webhook>> getWebhooks() {
		return this.guild.getWebhooks();
	}

	@Override
	public List<GuildVoiceState> getVoiceStates() {
		return this.guild.getVoiceStates();
	}

	@Override
	public VerificationLevel getVerificationLevel() {
		return this.guild.getVerificationLevel();
	}

	@Override
	public NotificationLevel getDefaultNotificationLevel() {
		return this.guild.getDefaultNotificationLevel();
	}

	@Override
	public MFALevel getRequiredMFALevel() {
		return this.guild.getRequiredMFALevel();
	}

	@Override
	public ExplicitContentLevel getExplicitContentLevel() {
		return this.guild.getExplicitContentLevel();
	}

	@Override
	public boolean checkVerification() {
		return this.guild.checkVerification();
	}

	@Override
	public boolean isAvailable() {
		return this.guild.isAvailable();
	}

	@Override
	public long getOwnerIdLong() {
		return this.guild.getOwnerIdLong();
	}

	@Override
	public RestAction<List<ListedEmote>> retrieveEmotes() {
		return this.guild.retrieveEmotes();
	}

	@Override
	public RestAction<ListedEmote> retrieveEmoteById(String id) {
		return this.guild.retrieveEmoteById(id);
	}

	@Override
	public RestAction<Ban> getBanById(String userId) {
		return this.guild.getBanById(userId);
	}

	@Override
	public List<Channel> getChannels(boolean includeHidden) {
		return this.guild.getChannels(includeHidden);
	}
}