package net.wuffy.core.util;

import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.wuffy.core.Core;
import net.wuffy.core.database.impl.IExtensionGuild;
import net.wuffy.core.database.impl.ImplGuild;
import net.wuffy.core.database.impl.ImplMember;

public class DiscordUtil {

	public static ImplMember searchMember(Core core, ImplGuild guild, String search) {
		if(search.matches("<@![0-9]{14,20}>"))
			return guild.getMemberById(Long.valueOf(search.substring(3, search.length() - 1)));

		if(search.matches("<@[0-9]{14,20}>"))
			return guild.getMemberById(Long.valueOf(search.substring(2, search.length() - 1)));

		if(search.matches("[0-9]{14,20}"))
			return guild.getMemberById(Long.valueOf(search));

		List<Member> found = guild.getMembersByNickname(search, true);

		if(found.isEmpty())
			found = guild.getMembersByName(search, true);

		if(found.isEmpty())
			found = guild.getMembers().stream().filter(member -> member.getEffectiveName().toLowerCase().startsWith(search.toLowerCase())).collect(Collectors.toList());

		return found.isEmpty() ? null : found.get(0) != null && found.get(0).getUser() != null ? core.getStorageService().getExtension(IExtensionGuild.class).getMemeber(guild, found.get(0)) : null;
	}

	public static TextChannel searchChannel(ImplGuild guild, String search) {
		if(search.matches("<@![0-9]{14,20}>"))
			return guild.getTextChannelById(Long.valueOf(search.substring(3, search.length() - 1)));

		if(search.matches("<@[0-9]{14,20}>"))
			return guild.getTextChannelById(Long.valueOf(search.substring(2, search.length() - 1)));

		if(search.matches("[0-9]{14,20}"))
			return guild.getTextChannelById(Long.valueOf(search));

		List<TextChannel> found = guild.getTextChannelsByName(search, true);

		if(found.isEmpty())
			found = guild.getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().startsWith(search.toLowerCase())).collect(Collectors.toList());

		return found.isEmpty() ? null : found.get(0) != null ? found.get(0) : null;
	}

	public static Role searchRole(ImplGuild guild, String search) {
		if(search.matches("<@![0-9]{14,20}>"))
			return guild.getRoleById(Long.valueOf(search.substring(3, search.length() - 1)));

		if(search.matches("<@[0-9]{14,20}>"))
			return guild.getRoleById(Long.valueOf(search.substring(2, search.length() - 1)));

		if(search.matches("[0-9]{14,20}"))
			return guild.getRoleById(Long.valueOf(search));

		if(search.equalsIgnoreCase("@everyone") || search.equalsIgnoreCase("everyone"))
			return guild.getPublicRole();

		List<Role> found = guild.getRolesByName(search, true);

		if(found.isEmpty())
			found = guild.getRoles().stream().filter(textChannel -> textChannel.getName().toLowerCase().startsWith(search.toLowerCase())).collect(Collectors.toList());

		return found.isEmpty() ? null : found.get(0) != null ? found.get(0) : null;
	}
}