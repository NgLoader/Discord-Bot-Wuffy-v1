package de.ngloader.bot.command.commands.admin;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import de.ngloader.core.util.DiscordUtil;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

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
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		WuffyMember member = event.getMember(WuffyMember.class);
		String locale = member.getLocale();

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

									this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED_USER, locale,
											"%c", Integer.toString(delete.size()),
											"%m", memberSelected.getEffectiveName()));
								} else
									this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_CLEAR_NO_MESAGE_TO_DELETE, locale));
							});
						} else
							this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_MEMBER_NOT_FOUND, locale, "%m", args[1]));
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

									this.sendMessage(event, MessageType.SUCCESS, i18n.format(TranslationKeys.MESSAGE_CLEAR_CLEARED, locale, "%c", Integer.toString(delete.size())));
								} else
									this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_CLEAR_NO_MESAGE_TO_DELETE, locale));
							});
						}
				} else
					this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NUMBER_OUT_OF_RANGE, locale));
				//Clear number out of range
			} else
				this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_NOT_A_NUMBER, locale));
			//Not a number
		} else
			this.sendMessage(event, MessageType.SYNTAX, i18n.format(TranslationKeys.MESSAGE_CLEAR_SYNTAX, locale));
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) { }
}