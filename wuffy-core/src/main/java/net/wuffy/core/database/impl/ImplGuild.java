package net.wuffy.core.database.impl;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.ListedEmote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.StoreChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VanityInvite;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.managers.GuildManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.MemberAction;
import net.dv8tion.jda.api.requests.restaction.RoleAction;
import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.ChannelOrderAction;
import net.dv8tion.jda.api.requests.restaction.order.RoleOrderAction;
import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.api.utils.cache.MemberCacheView;
import net.dv8tion.jda.api.utils.cache.SnowflakeCacheView;
import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView;
import net.dv8tion.jda.api.utils.concurrent.Task;
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
	public SortedSnowflakeCacheView<Category> getCategoryCache() {
		return this.guild.getCategoryCache();
	}

	@Override
	public SortedSnowflakeCacheView<TextChannel> getTextChannelCache() {
		return this.guild.getTextChannelCache();
	}

	@Override
	public SortedSnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
		return this.guild.getVoiceChannelCache();
	}

	@Override
	public SortedSnowflakeCacheView<Role> getRoleCache() {
		return this.guild.getRoleCache();
	}

	@Override
	public SnowflakeCacheView<Emote> getEmoteCache() {
		return this.guild.getEmoteCache();
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

	@SuppressWarnings("deprecation")
	@Override
	public boolean checkVerification() {
		return this.guild.checkVerification();
	}

	@SuppressWarnings("deprecation")
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
	public boolean isLoaded() {
		return this.guild.isLoaded();
	}

	@Override
	public void pruneMemberCache() {
		this.guild.pruneMemberCache();
	}

	@Override
	public boolean unloadMember(long userId) {
		return this.guild.unloadMember(userId);
	}

	@Override
	public int getMemberCount() {
		return this.guild.getMemberCount();
	}

	@SuppressWarnings("deprecation")
	@Override
	public RestAction<String> retrieveVanityUrl() {
		return this.guild.retrieveVanityUrl();
	}

	@Override
	public String getVanityCode() {
		return this.guild.getVanityCode();
	}

	@Override
	public RestAction<VanityInvite> retrieveVanityInvite() {
		return this.guild.retrieveVanityInvite();
	}

	@Override
	public String getDescription() {
		return this.guild.getDescription();
	}

	@Override
	public Locale getLocale() {
		return this.guild.getLocale();
	}

	@Override
	public String getBannerId() {
		return this.guild.getBannerId();
	}

	@Override
	public BoostTier getBoostTier() {
		return this.guild.getBoostTier();
	}

	@Override
	public int getBoostCount() {
		return this.guild.getBoostCount();
	}

	@Override
	public List<Member> getBoosters() {
		return this.guild.getBoosters();
	}

	@Override
	public int getMaxMembers() {
		return this.guild.getMaxMembers();
	}

	@Override
	public int getMaxPresences() {
		return this.guild.getMaxPresences();
	}

	@Override
	public RestAction<MetaData> retrieveMetaData() {
		return this.guild.retrieveMetaData();
	}

	@Override
	public TextChannel getRulesChannel() {
		return this.guild.getRulesChannel();
	}

	@Override
	public TextChannel getCommunityUpdatesChannel() {
		return this.guild.getCommunityUpdatesChannel();
	}

	@Override
	public SortedSnowflakeCacheView<StoreChannel> getStoreChannelCache() {
		return this.guild.getStoreChannelCache();
	}

	@Override
	public List<GuildChannel> getChannels(boolean includeHidden) {
		return this.guild.getChannels();
	}

	@Override
	public RestAction<List<Ban>> retrieveBanList() {
		return this.guild.retrieveBanList();
	}

	@Override
	public RestAction<Ban> retrieveBanById(String userId) {
		return this.guild.retrieveBanById(userId);
	}

	@Override
	public RestAction<Integer> retrievePrunableMemberCount(int days) {
		return this.guild.retrievePrunableMemberCount(days);
	}

	@Override
	public AuditLogPaginationAction retrieveAuditLogs() {
		return this.guild.retrieveAuditLogs();
	}

	@Override
	public RestAction<List<Invite>> retrieveInvites() {
		return this.guild.retrieveInvites();
	}

	@Override
	public RestAction<List<Webhook>> retrieveWebhooks() {
		return this.guild.retrieveWebhooks();
	}

	@SuppressWarnings("deprecation")
	@Override
	public CompletableFuture<Void> retrieveMembers() {
		return this.guild.retrieveMembers();
	}

	@Override
	public Task<Void> loadMembers(Consumer<Member> callback) {
		return this.guild.loadMembers(callback);
	}

	@Override
	public RestAction<Member> retrieveMemberById(long id, boolean update) {
		return this.guild.retrieveMemberById(id, update);
	}

	@Override
	public Task<List<Member>> retrieveMembersByIds(boolean includePresence, long... ids) {
		return this.guild.retrieveMembersByIds(includePresence, ids);
	}

	@Override
	public Task<List<Member>> retrieveMembersByPrefix(String prefix, int limit) {
		return this.guild.retrieveMembersByPrefix(prefix, limit);
	}

	@Override
	public RestAction<Void> moveVoiceMember(Member member, VoiceChannel voiceChannel) {
		return this.guild.moveVoiceMember(member, voiceChannel);
	}

	@Override
	public AuditableRestAction<Void> modifyNickname(Member member, String nickname) {
		return this.guild.modifyNickname(member, nickname);
	}

	@Override
	public AuditableRestAction<Integer> prune(int days, boolean wait, Role... roles) {
		return this.guild.prune(days, wait, roles);
	}

	@Override
	public AuditableRestAction<Void> kick(Member member, String reason) {
		return this.guild.kick(member, reason);
	}

	@Override
	public AuditableRestAction<Void> kick(String userId, String reason) {
		return this.guild.kick(userId, reason);
	}

	@Override
	public AuditableRestAction<Void> ban(User user, int delDays, String reason) {
		return this.guild.ban(user, delDays, reason);
	}

	@Override
	public AuditableRestAction<Void> ban(String userId, int delDays, String reason) {
		return this.guild.ban(userId, delDays, reason);
	}

	@Override
	public AuditableRestAction<Void> unban(String userId) {
		return this.guild.unban(userId);
	}

	@Override
	public AuditableRestAction<Void> deafen(Member member, boolean deafen) {
		return this.guild.deafen(member, deafen);
	}

	@Override
	public AuditableRestAction<Void> mute(Member member, boolean mute) {
		return this.guild.mute(member, mute);
	}

	@Override
	public AuditableRestAction<Void> addRoleToMember(Member member, Role role) {
		return this.guild.addRoleToMember(member, role);
	}

	@Override
	public AuditableRestAction<Void> removeRoleFromMember(Member member, Role role) {
		return this.guild.removeRoleFromMember(member, role);
	}

	@Override
	public AuditableRestAction<Void> modifyMemberRoles(Member member, Collection<Role> rolesToAdd,
			Collection<Role> rolesToRemove) {
		return this.guild.modifyMemberRoles(member, rolesToAdd, rolesToRemove);
	}

	@Override
	public AuditableRestAction<Void> modifyMemberRoles(Member member, Collection<Role> roles) {
		return this.guild.modifyMemberRoles(member, roles);
	}

	@Override
	public AuditableRestAction<Void> transferOwnership(Member newOwner) {
		return this.guild.transferOwnership(newOwner);
	}

	@Override
	public ChannelAction<TextChannel> createTextChannel(String name, Category parent) {
		return this.guild.createTextChannel(name, parent);
	}

	@Override
	public ChannelAction<VoiceChannel> createVoiceChannel(String name, Category parent) {
		return this.guild.createVoiceChannel(name, parent);
	}

	@Override
	public ChannelAction<Category> createCategory(String name) {
		return this.guild.createCategory(name);
	}

	@Override
	public RoleAction createRole() {
		return this.guild.createRole();
	}

	@Override
	public AuditableRestAction<Emote> createEmote(String name, Icon icon, Role... roles) {
		return this.guild.createEmote(name, icon, roles);
	}

	@Override
	public ChannelOrderAction modifyCategoryPositions() {
		return this.guild.modifyCategoryPositions();
	}

	@Override
	public ChannelOrderAction modifyTextChannelPositions() {
		return this.guild.modifyTextChannelPositions();
	}

	@Override
	public ChannelOrderAction modifyVoiceChannelPositions() {
		return this.guild.modifyVoiceChannelPositions();
	}

	@Override
	public CategoryOrderAction modifyTextChannelPositions(Category category) {
		return this.guild.modifyTextChannelPositions(category);
	}

	@Override
	public CategoryOrderAction modifyVoiceChannelPositions(Category category) {
		return this.guild.modifyVoiceChannelPositions(category);
	}

	@Override
	public RoleOrderAction modifyRolePositions(boolean useAscendingOrder) {
		return this.guild.modifyRolePositions(useAscendingOrder);
	}
}