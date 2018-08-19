package net.wuffy.core.database.impl;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;
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
	public List<Permission> getPermissions() {
		return this.member.getPermissions();
	}

	@Override
	public boolean hasPermission(Permission... permissions) {
		return this.member.hasPermission(permissions);
	}

	@Override
	public boolean hasPermission(Collection<Permission> permissions) {
		return this.member.hasPermission(permissions);
	}

	@Override
	public boolean hasPermission(Channel channel, Permission... permissions) {
		return this.member.hasPermission(channel, permissions);
	}

	@Override
	public boolean hasPermission(Channel channel, Collection<Permission> permissions) {
		return this.member.hasPermission(channel, permissions);
	}

	public <T extends ImplUser> T getUser(Class<T> userClass) {
		return userClass.cast(this.getUser());
	}

	@Override
	public ImplUser getUser() {
		return ImplUser.class.cast(this.user);
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
	public OffsetDateTime getJoinDate() {
		return this.member.getJoinDate();
	}

	@Override
	public GuildVoiceState getVoiceState() {
		return this.member.getVoiceState();
	}

	@Override
	public Game getGame() {
		return this.member.getGame();
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
	public List<Permission> getPermissions(Channel channel) {
		return this.member.getPermissions(channel);
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
}