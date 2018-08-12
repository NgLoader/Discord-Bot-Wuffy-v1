package de.ngloader.bot.command.commands.admin;

import java.util.Arrays;
import java.util.stream.Collectors;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

@CommandSettings(
		category = CommandCategory.ADMIN,
		guildPermissionRequierd = { Permission.BAN_MEMBERS },
		memberPermissionList = { PermissionKeys.COMMAND_SOFTBAN },
		memberPermissionRequierd = { PermissionKeys.COMMAND_SOFTBAN },
		aliases = { "softban", "sban" })
public class CommandSoftBan extends Command {

	public CommandSoftBan(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		String locale = member.getLocale();

		if(args.length > 0) {
			Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args[0]);

			if(memberSelected != null)
				if(guild.hasHighestRole(member, memberSelected))
					if(guild.getSelfMember().canInteract(memberSelected)) {
						var reason = args.length > 1 ? Arrays.asList(Arrays.copyOfRange(args, 1, args.length)).stream().collect(Collectors.joining(" ")) : "";

						event.getMessage().delete().queue();

						//TODO add to ban history

						if(reason.isEmpty()) {
							event.getGuild().getController().ban(memberSelected, 0, reason).queue(success -> event.getGuild().getController().unban(memberSelected.getUser()).queue());
								this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_SOFTBAN, locale,
									"%m", memberSelected.getEffectiveName(),
									"%d", Integer.toString(0)));
						} else {
							event.getGuild().getController().ban(memberSelected, 0, reason).queue(success -> event.getGuild().getController().unban(memberSelected.getUser()).queue());
							this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_SOFTBAN_REASON, locale,
									"%m", memberSelected.getEffectiveName(),
									"%r", reason));
						}
					} else
						this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_BOT_NO_INTERACT, locale, "%m", memberSelected.getEffectiveName()));
				else
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
			else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[0]));
		} else
			this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_SOFTBAN_SYNTAX, locale));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}