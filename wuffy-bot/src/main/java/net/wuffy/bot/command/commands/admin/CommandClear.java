package net.wuffy.bot.command.commands.admin;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;
import net.wuffy.core.util.DiscordUtil;

@CommandSettings(
		category = CommandCategory.ADMIN,
		guildPermissionRequierd = { Permission.MESSAGE_MANAGE },
		memberPermissionList = { PermissionKeys.COMMAND_CLEAR },
		memberPermissionRequierd = { PermissionKeys.COMMAND_CLEAR },
		aliases = { "clear", "purge" })
public class CommandClear extends Command {

	public CommandClear(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		String locale = member.getLocale();

		if(args.getSize() > 0) {
			if(args.get(0).matches("[0-9]{1,4}")) {
				int count = Integer.valueOf(args.get(0)) + 1;

				if(count == 1001) {
					count--;
					event.getMessage().delete().complete();
				}

				if(count > 0 && count < 1001) {
					MessageHistory history = new MessageHistory(event.getChannel());
//					MessageBuilder messageBuilder = new MessageBuilder(event, MessageType.LOADING);
//					messageBuilder.append(i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARING, locale));
//					messageBuilder.complete();

					if(args.getSize() > 1) {
						Member memberSelected = DiscordUtil.searchMember(event.getCore(), event.getGuild(), args.get(1));

						if(memberSelected != null) {
							int countSize = count - 1;
							int requestCount = countSize;
							int deleted = 0;

							do {
								int request = requestCount > 100 ? 100 : requestCount;
								requestCount -= request;

								if(request < 1)
									break;

								List<String> delete = history.retrievePast(request).complete().stream()
											.filter(message ->
												message.getIdLong() != event.getMessageIdLong() &&
												message.getAuthor().getIdLong() == memberSelected.getUser().getIdLong() &&
												message.getTimeCreated().isAfter(OffsetDateTime.now().minusWeeks(2).plusMinutes(2)))
											.map(message -> Long.toString(message.getIdLong()))
											.collect(Collectors.toList());

								deleted += delete.size();

								if(delete.size() > 1)
									event.getTextChannel().deleteMessagesByIds(delete).queue();
								else
									event.getTextChannel().deleteMessageById(delete.get(0)).queue();
							} while (requestCount > 1);

							if(deleted > 0) {
//								messageBuilder.editMessage(i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED_USER, locale,
//										"%c", Integer.toString(deleted),
//										"%m", memberSelected.getEffectiveName()));
								this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED_USER, locale,
										"%c", Integer.toString(deleted),
										"%m", memberSelected.getEffectiveName()));
							} else
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_CLEAR_NO_MESAGE_TO_DELETE, locale));
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args.get(1)));
						//Member can not found
						} else {
							int countSize = count - 1;
							int requestCount = countSize;
							int deleted = 0;

							do {
								int request = requestCount > 100 ? 100 : requestCount;
								requestCount -= request;

								if(request < 1)
									break;

								List<String> delete = history.retrievePast(request).complete().stream()
											.filter(message ->
												message.getIdLong() != event.getMessageIdLong() &&
												message.getTimeCreated().isAfter(OffsetDateTime.now().minusWeeks(2).plusMinutes(2)))
											.map(message -> Long.toString(message.getIdLong()))
											.collect(Collectors.toList());

								deleted += delete.size();

								if(delete.size() > 1)
									event.getTextChannel().deleteMessagesByIds(delete).queue();
								else if (delete.size() > 0)
									event.getTextChannel().deleteMessageById(delete.get(0)).queue();
							} while (requestCount > 1);

							if(deleted > 0) {
//								messageBuilder.editMessage(i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED, locale, "%c", Integer.toString(deleted)));
								this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED, locale, "%c", Integer.toString(deleted)));
							} else
								this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_CLEAR_NO_MESAGE_TO_DELETE, locale));
						}
				} else
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NUMBER_OUT_OF_RANGE, locale));
				//Clear number out of range
			} else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOT_A_NUMBER, locale));
			//Not a number
		} else
			this.sendHelpMessage(event, command, args);
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) { }
}