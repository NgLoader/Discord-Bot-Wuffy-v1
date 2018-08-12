package de.ngloader.bot.command.commands.nsfw;

import de.ngloader.bot.command.CommandHandler;
import de.ngloader.bot.command.commands.Command;
import de.ngloader.bot.command.commands.CommandCategory;
import de.ngloader.bot.command.commands.CommandSettings;
import de.ngloader.bot.command.commands.MessageType;
import de.ngloader.bot.command.commands.image.rra.EnumRRATypes;
import de.ngloader.bot.command.commands.image.rra.RRABuilder;
import de.ngloader.bot.database.guild.WuffyMember;
import de.ngloader.bot.database.user.WuffyUser;
import de.ngloader.bot.keys.PermissionKeys;
import de.ngloader.bot.keys.TranslationKeys;
import de.ngloader.core.event.WuffyMessageRecivedEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;

@CommandSettings(
		category = CommandCategory.NSFW,
		guildPermissionRequierd = { Permission.MESSAGE_EMBED_LINKS },
		memberPermissionList = { PermissionKeys.COMMAND_GTN },
		memberPermissionRequierd = { PermissionKeys.COMMAND_GTN },
		aliases = { "gtn" },
		privateChatCommand = true,
		nsfw = true)
public class CommandRRAGtn extends Command {

	public CommandRRAGtn(CommandHandler handler) {
		super(handler);
	}

	@Override
	public void onGuild(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendImage(event, event.getMember(WuffyMember.class).getLocale());
	}

	@Override
	public void onPrivate(WuffyMessageRecivedEvent event, String command, String[] args) {
		this.sendImage(event, event.getAuthor(WuffyUser.class).getUserLocale("en-US"));
	}

	public void sendImage(WuffyMessageRecivedEvent event, String locale) {
		Message message = event.getChannel().sendMessage(this.createEmbed(event, MessageType.LOADING)
				.appendDescription(i18n.format(TranslationKeys.MESSAGE_IMAGE_SEARCHING, locale))
				.build()).complete();

		String picture = RRABuilder.getRandom(EnumRRATypes.NSFW_GTN);

		if(picture != null) {
			this.queue(event, MessageType.PICTURE, message.editMessage(this.createEmbed(event, MessageType.PICTURE).setImage(picture).build()));
		} else
			this.queue(event, MessageType.SYNTAX, message.editMessage(this.createEmbed(event, MessageType.SYNTAX)
					.appendDescription(this.i18n.format(TranslationKeys.MESSAGE_NSFW_NOTHING_FOUND, locale)).build()));
	}
}