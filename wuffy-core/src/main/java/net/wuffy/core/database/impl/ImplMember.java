package net.wuffy.core.database.impl;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ClientType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import net.wuffy.core.Core;

public class ImplMember implements Member {

	protected final Core core;

	protected final ImplGuild guild;
	protected final ImplUser user;
	protected final Member member;

	public ImplMember(Core core, ImplUser user, Member member, ImplGuild guild) {
		this.core = core;
		this.user = user;
		this.member = member;
		this.guild = guild;
	}

	public Core getCore() {
		return this.core;
	}

	public Member getRealMember() {
		return this.member;
	}

	@Override
	public String getAsMention() {
		return this.member.getAsMention();
	}

	@Override
	public boolean hasPermission(Permission... permissions) {
		return this.member.hasPermission(permissions);
	}

	@Override
	public boolean hasPermission(Collection<Permission> permissions) {
		return this.member.hasPermission(permissions);
	}

	public <T extends ImplUser> T getUser(Class<T> userClass) {
		return userClass.cast(this.getUser());
	}

	public <T extends ImplGuild> T getGuild(Class<T> guildClass) {
		return guildClass.cast(this.guild);
	}

	@Override
	public ImplGuild getGuild() {
		return ImplGuild.class.cast(this.guild);
	}

	@Override
	public JDA getJDA() {
		return this.member.getJDA();
	}

	@Override
	public GuildVoiceState getVoiceState() {
		return this.member.getVoiceState();
	}

	@Override
	public OnlineStatus getOnlineStatus() {
		return this.member.getOnlineStatus();
	}

	@Override
	public String getNickname() {
		return this.member.getNickname();
	}

	@Override
	public String getEffectiveName() {
		return this.member.getEffectiveName();
	}

	@Override
	public List<Role> getRoles() {
		return this.member.getRoles();
	}

	@Override
	public Color getColor() {
		return this.member.getColor();
	}

	@Override
	public int getColorRaw() {
		return this.member.getColorRaw();
	}

	@Override
	public boolean canInteract(Member member) {
		if(member instanceof ImplMember)
			return PermissionUtil.canInteract(this.member, ((ImplMember) member).getRealMember()); //TODO push a fix to JDA (PermissionUtil check guildId and not guild class)
		return PermissionUtil.canInteract(this.member, member);
	}

	@Override
	public boolean canInteract(Role role) {
		return PermissionUtil.canInteract(this.member, role);
	}

	@Override
	public boolean canInteract(Emote emote) {
		return PermissionUtil.canInteract(this.member, emote);
	}

	@Override
	public boolean isOwner() {
		return this.member.isOwner();
	}

	@Override
	public TextChannel getDefaultChannel() {
		return this.member.getDefaultChannel();
	}

	@Override
	public long getIdLong() {
		return this.member.getIdLong();
	}

	@Override
	public EnumSet<Permission> getPermissions() {
		return this.member.getPermissions();
	}

	@Override
	public EnumSet<Permission> getPermissions(GuildChannel channel) {
		return this.member.getPermissions(channel);
	}

	@Override
	public EnumSet<Permission> getPermissionsExplicit() {
		return this.member.getPermissionsExplicit();
	}

	@Override
	public EnumSet<Permission> getPermissionsExplicit(GuildChannel channel) {
		return this.member.getPermissionsExplicit(channel);
	}

	@Override
	public boolean hasPermission(GuildChannel channel, Permission... permissions) {
		return this.member.hasPermission(channel, permissions);
	}

	@Override
	public boolean hasPermission(GuildChannel channel, Collection<Permission> permissions) {
		return this.member.hasPermission(channel, permissions);
	}

	@Override
	public boolean canSync(GuildChannel targetChannel, GuildChannel syncSource) {
		return this.member.canSync(targetChannel, syncSource);
	}

	@Override
	public boolean canSync(GuildChannel channel) {
		return this.member.canSync(channel);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFake() {
		return this.member.isFake();
	}

	@Override
	public User getUser() {
		return this.member.getUser();
	}

	@Override
	public OffsetDateTime getTimeJoined() {
		return this.member.getTimeJoined();
	}

	@Override
	public boolean hasTimeJoined() {
		return this.member.hasTimeJoined();
	}

	@Override
	public OffsetDateTime getTimeBoosted() {
		return this.member.getTimeBoosted();
	}

	@Override
	public List<Activity> getActivities() {
		return this.member.getActivities();
	}

	@Override
	public OnlineStatus getOnlineStatus(ClientType type) {
		return this.member.getOnlineStatus(type);
	}

	@Override
	public EnumSet<ClientType> getActiveClients() {
		return this.member.getActiveClients();
	}

	@Override
	public boolean isPending() {
		return this.member.isPending();
	}
}