package net.wuffy.bot.command.commands.admin;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyGuild;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;
import net.wuffy.core.util.DiscordUtil;

@CommandSettings(
		category = CommandCategory.ADMIN,
		guildPermissionRequierd = { Permission.BAN_MEMBERS },
		memberPermissionList = { PermissionKeys.COMMAND_BAN },
		memberPermissionRequierd = { PermissionKeys.COMMAND_BAN },
		aliases = { "ban" })
public class CommandBan extends Command {

	public CommandBan(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		String locale = member.getLocale();

		if(args.getSize() > 0) {
			Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args.get(0));

			if(memberSelected != null)
				if(guild.hasHighestRole(member, memberSelected))
					if(guild.getSelfMember().canInteract(memberSelected)) {
						var days = -1;
						if(args.getSize() > 1)
							if(args.get(1).matches("[0-9]{1,4}"))
								days = Integer.parseInt(args.get(1));

						var reason = args.getSize() > (days != -1 ? 2 : 1) ? Arrays.asList(Arrays.copyOfRange(args.getArguments(), days != -1 ? 2 : 1, args.getSize())).stream().collect(Collectors.joining(" ")) : "";

						event.getMessage().delete().queue();

						//TODO add to ban history

						if(reason.isEmpty()) {
							event.getGuild().ban(memberSelected, days != -1 ? days : 0).queue();
							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_BAN, locale,
									"%m", memberSelected.getEffectiveName(),
									"%d", Integer.toString(days != -1 ? days : 0),
									"%r", "Unknown"));
						} else {
							event.getGuild().ban(memberSelected, days != -1 ? days : 0, reason).queue();
							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_BAN_REASON, locale,
									"%m", memberSelected.getEffectiveName(),
									"%d", Integer.toString(days != -1 ? days : 0),
									"%r", reason));
						}
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_BOT_NO_INTERACT, locale, "%m", memberSelected.getEffectiveName()));
				 else
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
			else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args.get(0)));
			//Member not found
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}