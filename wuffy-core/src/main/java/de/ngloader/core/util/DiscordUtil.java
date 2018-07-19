package de.ngloader.core.util;

import java.util.List;
import java.util.stream.Collectors;

import de.ngloader.core.Core;
import de.ngloader.core.database.impl.ImplGuild;
import net.dv8tion.jda.core.entities.Member;

public class DiscordUtil {

	public static Member searchMember(Core core, ImplGuild guild, String input) {
		if(input.matches("<@[0-9]{14,20}>"))
			return guild.getMemberById(input);

		if(input.matches("[0-9]{14,20}"))
			return guild.getMemberById(input);

		List<Member> found = guild.getMembersByNickname(input, true);

		if(found.isEmpty())
			found = guild.getMembersByName(input, true);

		if(found.isEmpty())
			found = guild.getMembers().stream().filter(member -> member.getEffectiveName().toLowerCase().startsWith(input.toLowerCase())).collect(Collectors.toList());

		return found.isEmpty() ? null : found.get(0);
	}
}