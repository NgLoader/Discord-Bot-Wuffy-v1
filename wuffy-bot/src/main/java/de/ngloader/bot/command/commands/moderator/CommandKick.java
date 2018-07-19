package de.ngloader.bot.command.commands.moderator;

import java.util.Arrays;
import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.entities.Member;

@Command(aliases = { "kick" })
@CommandConfig(category = CommandCategory.MODERATOR)
public class CommandKick extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		WuffyGuild guild = event.getGuild(WuffyGuild.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_KICK))
			if(args.length > 0) {
				Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args[0]);

				if(memberSelected != null) {
					if(guild.hasHighestRole(member, memberSelected)) {
						if(guild.getSelfMember().canInteract(memberSelected)) {
							var reason = args.length > 1 ? Arrays.asList(Arrays.copyOfRange(args, 1, args.length)).stream().collect(Collectors.joining(" ")) : "";

							//TODO add to ban history

							if(reason.isEmpty()) {
								event.getGuild().getController().kick(memberSelected).queue();
								this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_KICK_KICKED, locale, "%m", memberSelected.getEffectiveName()));
							} else {
								event.getGuild().getController().kick(memberSelected, reason).queue();
								this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_KICK_KICKED_REASON, locale, "%m", memberSelected.getEffectiveName(), "%r", reason));
							}
						} else
							this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_BOT_NO_INTERACT, locale, "%m", memberSelected.getEffectiveName()));
					} else
						this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_LOWER_ROLE, locale, "%m", memberSelected.getEffectiveName()));
				} else
					this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[0]));
				//Member not found
			} else
				this.replay(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_KICK_SYNTAX, locale));
			//No args
		else
			this.replay(event, MessageType.ERROR, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_KICK.key));
	}
}