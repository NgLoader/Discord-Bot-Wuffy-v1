package de.ngloader.bot.command.commands.moderator;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.command.MessageType;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.lang.I18n;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

@Command(aliases = { "clear", "purge", "cls" })
@CommandConfig(category = CommandCategory.MODERATOR)
public class CommandClear extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		I18n i18n = event.getCore().getI18n();
		String locale = member.getLocale();

		if(member.hasPermission(event.getTextChannel(), PermissionKeys.COMMAND_CLEAR)) {
			if(args.length > 0) {
				if(args[0].matches("[0-9]{1,4}")) {
					int count = Integer.valueOf(args[0]) + 1;

					if(count == 101) {
						count--;
						event.getMessage().delete().complete();
					}

					if(count > 0 && count < 101) {
						MessageHistory history = new MessageHistory(event.getChannel());

						if(args.length > 1) {
							Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args[1]);

							if(memberSelected != null) {
								history.retrievePast(count).queue(messages -> {
									var delete = messages.stream()
											.filter(msg ->
												msg.getIdLong() != event.getMessageIdLong() &&
												msg.getAuthor().getIdLong() == memberSelected.getUser().getIdLong() &&
												msg.getCreationTime().isAfter(OffsetDateTime.now().minusWeeks(2).plusMinutes(2)))
											.collect(Collectors.toList());

									if(!delete.isEmpty()) {
										if(delete.size() == 1)
											delete.get(0).delete().queue();
										else
											event.getTextChannel().deleteMessages(delete).queue();

										this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED_USER, locale,
												"%c", Integer.toString(delete.size()),
												"%m", memberSelected.getEffectiveName()));
									} else
										this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_CLEAR_NO_MESAGE_TO_DELETE, locale));
								});
							} else
								this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[1]));
							//Member can not found
							} else {
								history.retrievePast(count).queue(messages -> {
									List<Message> delete = messages.stream()
											.filter(msg ->
												msg.getIdLong() != event.getMessageIdLong() &&
												msg.getCreationTime().isAfter(OffsetDateTime.now().minusWeeks(2).plusMinutes(2)))
											.collect(Collectors.toList());

									if(!delete.isEmpty()) {
										if(delete.size() == 1)
											delete.get(0).delete().queue();
										else
											event.getTextChannel().deleteMessages(delete).queue();

										this.replay(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED, locale, "%c", Integer.toString(delete.size())));
									} else
										this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_CLEAR_NO_MESAGE_TO_DELETE, locale));
								});
							}
					} else
						this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NUMBER_OUT_OF_RANGE, locale));
					//Clear number out of range
				} else
					this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_NOT_A_NUMBER, locale));
				//Not a number
			} else
				this.replay(event, MessageType.WARN, i18n.format(TranslationKeys.MESSAGE_CLEAR_SYNTAX, locale));
			//False args
		} else
			this.replay(event, MessageType.PERMISSION, i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", PermissionKeys.COMMAND_CLEAR.key));
		//No permission
	}
}