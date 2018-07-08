package de.ngloader.bot.command.commands.moderator;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

import de.ngloader.bot.command.BotCommand;
import de.ngloader.bot.command.CommandCategory;
import de.ngloader.bot.command.CommandConfig;
import de.ngloader.bot.database.guild.WuffyGuild;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.lang.TranslationKeys;
import de.ngloader.core.command.Command;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageHistory;

@Command(aliases = { "clear", "purge", "cls" })
@CommandConfig(category = CommandCategory.MODERATOR)
public class CommandClear extends BotCommand {

	@Override
	public void execute(WuffyMessageRecivedEvent event, String[] args) {
		var executer = event.getMember(WuffyMember.class);
		var locale = event.getGuild(WuffyGuild.class).getLocale();
		var i18n = event.getCore().getI18n();

		if(executer.hasPermission(event.getChannel().getIdLong(), "command.clear")) {
			if(args.length > 0) {
				if(args[0].matches("[0-9]{1,4}")) {
					int count = Integer.valueOf(args[0]);

					if(count > 0 && count < 101) {
						event.getMessage().delete().complete();
						MessageHistory history = new MessageHistory(event.getChannel());

						if(args.length > 1) {
							if(args[1].matches("<@[0-9]{14,20}>")) {
								Member member = event.getGuild().getMemberById(args[1].substring(2, args[1].length() - 1));
		
								if(member != null) {
									history.retrievePast(count).queue(messages -> {
										var delete = messages.stream()
												.filter(msg -> 
													msg.getAuthor().getIdLong() == member.getUser().getIdLong() &&
													msg.getCreationTime().isAfter(OffsetDateTime.now().minusWeeks(2).plusMinutes(2)))
												.collect(Collectors.toList());

										if(!delete.isEmpty()) {
											if(delete.size() == 1)
												delete.get(0).delete().queue();
											else
												event.getTextChannel().deleteMessages(delete).queue();

											this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED_USER, locale,
													"%c", Integer.toString(delete.size()),
													"%m", member.getEffectiveName()));
										} else
											this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_NO_MESAGES_TO_DELETE, locale, "%m", args[1].substring(2, args[1].length() - 1)));
									});
								} else
									this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_MEMBER_NOT_FOUND, locale, "%m", args[1].substring(2, args[1].length() - 1)));
								//Member can not found
							} else
								this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_USER_CAN_NOT_EXIST, locale));
							//Member can not exist
							} else {
								history.retrievePast(count).queue(messages -> {
									var delete = messages.stream()
											.filter(msg ->
												msg.getCreationTime().isAfter(OffsetDateTime.now().minusWeeks(2).plusMinutes(2)))
											.collect(Collectors.toList());

									if(!delete.isEmpty()) {
										if(delete.size() == 1)
											delete.get(0).delete().queue();
										else
											event.getTextChannel().deleteMessages(delete).queue();

										this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED, locale, "%c", Integer.toString(delete.size())));
									} else
										this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_NO_MESAGES_TO_DELETE, locale));
								});
							}
					} else
						this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_NUMBER_OUT_OF_RANGE, locale));
					//Clear number out of range
				} else
					this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_NOT_A_NUMBER, locale));
				//Not a number
			} else
				this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_CLEAR_FALSE_ARGS, locale));
			//False args
		} else
			this.replay(event.getChannel(), i18n.format(TranslationKeys.MESSAGE_NO_PERMISSION, locale, "%p", "command.clear"));
		//No permission
	}
}