package de.ngloader.core.database.impl;

import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

import de.ngloader.core.Core;
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

public class ImplMemeber implements Member {

	protected final Core core;
	protected final Member member;

	public ImplMemeber(Core core, Member member) {
		this.core = core;
		this.member = member;
	}

	public Core getCore() {
		return core;
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

	@Override
	public ImplUser getUser() {
		return this.core.getStorageService().getExtension(IExtensionUser.class).getUser(this.member.getUser().getIdLong());
	}

	@Override
	public ImplGuild getGuild() {
		return this.core.getStorageService().getExtension(IExtensionGuild.class).getGuild(this.member.getGuild().getIdLong());
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
		return this.member.canInteract(member);
	}

	@Override
	public boolean canInteract(Role role) {
		return this.member.canInteract(role);
	}

	@Override
	public boolean canInteract(Emote emote) {
		return this.member.canInteract(emote);
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