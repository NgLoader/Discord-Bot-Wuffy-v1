package net.wuffy.bot.command.commands.image.rra;

import net.dv8tion.jda.api.entities.Message;
import net.wuffy.bot.command.CommandHandler;
import net.wuffy.bot.command.commands.Command;
import net.wuffy.bot.command.commands.CommandCategory;
import net.wuffy.bot.command.commands.CommandSettings;
import net.wuffy.bot.command.commands.MessageType;
import net.wuffy.bot.database.guild.WuffyMember;
import net.wuffy.bot.database.user.WuffyUser;
import net.wuffy.bot.keys.PermissionKeys;
import net.wuffy.bot.keys.TranslationKeys;
import net.wuffy.core.event.WuffyMessageRecivedEvent;
import net.wuffy.core.util.ArgumentBuffer;

@CommandSettings(
		category = CommandCategory.IMAGE,
		memberPermissionList = { PermissionKeys.COMMAND_CRY },
		memberPermissionRequierd = { PermissionKeys.COMMAND_CRY },
		aliases = { "cry", "crying" },
		privateChatCommand = true)
public class CommandRRACry extends Command {

	public CommandRRACry(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		this.sendImage(event, event.getMember(WuffyMember.class).getLocale());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, ArgumentBuffer args) {
		this.sendImage(event, event.getAuthor(WuffyUser.class).getUserLocale("en-US"));
	}

	public void sendImage(WuffyMessageRecivedEvent event, String locale) {
		Message message = event.getChannel().sendMessage(this.createEmbed(event, MessageType.LOADING)
				.appendDescription(i18n.format(TranslationKeys.MESSAGE_IMAGE_SEARCHING, locale))
				.build()).complete();

		String picture = RRABuilder.getRandom(EnumRRATypes.CRY);

		if(picture != null)
			this.queue(event, MessageType.PICTURE, message.editMessage(this.createEmbed(event, MessageType.PICTURE).setImage(picture).build()));
		else
			this.queue(event, MessageType.SYNTAX, message.editMessage(this.createEmbed(event, MessageType.SYNTAX)
					.appendDescription(this.i18n.format(TranslationKeys.MESSAGE_IMAGE_NOTHING_FOUND, locale)).build()));
	}
}